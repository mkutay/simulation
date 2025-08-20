package api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A comprehensive WebSocket server implementation following RFC 6455 specification.
 * Supports all WebSocket frame types, proper handshakes, and handles various payload sizes.
 * Provides customizable callback methods for connection lifecycle and message handling.
 * (Doesn't handle fragmented messages, for now)
 *
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public abstract class WebSocketServer {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
  
  // WebSocket Protocol Constants
  private static final String WEBSOCKET_MAGIC_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
  private static final int MAX_FRAME_SIZE = 512 * 1024; // 512 KB default max frame size
  private static final int BUFFER_SIZE = 8192;
  
  // WebSocket Opcodes (RFC 6455)
  public static final byte OPCODE_CONTINUATION = 0x0;
  public static final byte OPCODE_TEXT = 0x1;
  public static final byte OPCODE_BINARY = 0x2;
  public static final byte OPCODE_CLOSE = 0x8;
  public static final byte OPCODE_PING = 0x9;
  public static final byte OPCODE_PONG = 0xA;
  
  // WebSocket Close Status Codes (RFC 6455)
  public static final int CLOSE_NORMAL = 1000;
  public static final int CLOSE_GOING_AWAY = 1001;
  public static final int CLOSE_PROTOCOL_ERROR = 1002;
  public static final int CLOSE_UNSUPPORTED_DATA = 1003;
  public static final int CLOSE_NO_STATUS = 1005;
  public static final int CLOSE_ABNORMAL = 1006;
  public static final int CLOSE_INVALID_PAYLOAD = 1007;
  public static final int CLOSE_POLICY_VIOLATION = 1008;
  public static final int CLOSE_MESSAGE_TOO_LARGE = 1009;
  public static final int CLOSE_EXTENSION_REQUIRED = 1010;
  public static final int CLOSE_INTERNAL_ERROR = 1011;
  
  // Server Configuration
  private final int port;
  private final int maxFrameSize;
  
  // Server State
  private ServerSocketChannel serverChannel;
  private Selector selector;
  private volatile boolean running = false;
  private ExecutorService executorService;
  
  // Client Management
  private final Set<WebSocketConnection> connections = ConcurrentHashMap.newKeySet();
  private final Pattern handshakePattern = Pattern.compile("Sec-WebSocket-Key: (.+)");
  
  /**
   * Create a WebSocket server with default configuration.
   * @param port The port to listen on
   */
  public WebSocketServer(int port) {
    this(port, MAX_FRAME_SIZE);
  }
  
  /**
   * Create a WebSocket server with custom configuration.
   * @param port The port to listen on
   * @param maxFrameSize Maximum frame size in bytes
   * @param enablePingPong Whether to enable automatic ping/pong
   * @param pingInterval Ping interval in milliseconds
   */
  public WebSocketServer(int port, int maxFrameSize) {
    this.port = port;
    this.maxFrameSize = maxFrameSize;
    this.executorService = Executors.newCachedThreadPool(r -> {
      Thread t = new Thread(r, "WebSocket-Worker");
      t.setDaemon(true);
      return t;
    });
  }
  
  /**
   * Called when a new client successfully completes the WebSocket handshake.
   * Override this method to handle new connections.
   * @param connection The new WebSocket connection
   */
  abstract public void onOpen(WebSocketConnection connection);
  
  /**
   * Called when a client disconnects or connection is closed.
   * Override this method to handle client disconnections.
   * @param connection The WebSocket connection that was closed
   * @param statusCode The close status code
   * @param reason The close reason
   */
  abstract public void onClose(WebSocketConnection connection, int statusCode, String reason);
  
  /**
   * Called when a text message is received from a client.
   * Override this method to handle incoming text messages.
   * @param connection The WebSocket connection that sent the message
   * @param message The text message received
   */
  abstract public void onMessage(WebSocketConnection connection, String message);
  
  /**
   * Called when a binary message is received from a client.
   * Override this method to handle incoming binary messages.
   * @param connection The WebSocket connection that sent the message
   * @param data The binary data received
   */
  abstract public void onBinaryMessage(WebSocketConnection connection, byte[] data);
  
  /**
   * Called when a ping frame is received from a client.
   * Default implementation automatically sends a pong response.
   * Override this method to customize ping handling.
   * @param connection The WebSocket connection that sent the ping
   * @param payload The ping payload
   */
  public void onPing(WebSocketConnection connection, byte[] payload) {
    logger.debug("Ping received from {}", connection.getRemoteAddress());
    try {
      connection.sendPong(payload);
    } catch (IOException e) {
      logger.warn("Failed to send pong response", e);
      onError(connection, e);
    }
  }
  
  /**
   * Called when a pong frame is received from a client.
   * Override this method to handle pong frames.
   * @param connection The WebSocket connection that sent the pong
   * @param payload The pong payload
   */
  public void onPong(WebSocketConnection connection, byte[] payload) {
    logger.debug("Pong received from {}", connection.getRemoteAddress());
    connection.updateLastPongTime();
  }
  
  /**
   * Called when an error occurs with a client connection.
   * Override this method to handle errors.
   * @param connection The WebSocket connection that encountered an error
   * @param error The exception that occurred
   */
  abstract public void onError(WebSocketConnection connection, Exception error);
  
  /**
   * Send a text message to a specific client.
   * @param connection The target connection
   * @param message The text message to send
   * @throws IOException If an I/O error occurs
   */
  public void sendMessage(WebSocketConnection connection, String message) throws IOException {
    if (!connections.contains(connection)) {
      throw new IllegalArgumentException("Connection is not managed by this server");
    }
    connection.sendText(message);
  }
  
  /**
   * Send binary data to a specific client.
   * @param connection The target connection
   * @param data The binary data to send
   * @throws IOException If an I/O error occurs
   */
  public void sendBinaryMessage(WebSocketConnection connection, byte[] data) throws IOException {
    if (!connections.contains(connection)) {
      throw new IllegalArgumentException("Connection is not managed by this server");
    }
    connection.sendBinary(data);
  }
  
  /**
   * Send a ping frame to a specific client.
   * @param connection The target connection
   * @param payload Optional ping payload
   * @throws IOException If an I/O error occurs
   */
  public void sendPing(WebSocketConnection connection, byte[] payload) throws IOException {
    if (!connections.contains(connection)) {
      throw new IllegalArgumentException("Connection is not managed by this server");
    }
    connection.sendPing(payload);
  }
  
  /**
   * Close a specific connection gracefully.
   * @param connection The connection to close
   * @param statusCode The close status code
   * @param reason The close reason
   */
  public void closeConnection(WebSocketConnection connection, int statusCode, String reason) {
    if (!connections.contains(connection)) {
      return;
    }
    
    try {
      connection.close(statusCode, reason);
    } catch (IOException e) {
      logger.warn("Error closing connection {}: {}", connection.getId(), e.getMessage());
    } finally {
      connections.remove(connection);
      onClose(connection, statusCode, reason);
    }
  }
  
  /**
   * Close a connection with default status code (1000 - Normal Closure).
   * @param connection The connection to close
   */
  public void closeConnection(WebSocketConnection connection) {
    closeConnection(connection, CLOSE_NORMAL, "Normal closure");
  }
  
  /**
   * Broadcast a text message to all connected clients.
   * 
   * @param message The message to broadcast
   */
  public void broadcast(String message) {
    if (connections.isEmpty()) {
      return;
    }
    
    connections.forEach(connection -> {
      try {
        connection.sendText(message);
      } catch (IOException e) {
        logger.warn("Failed to send broadcast message to {}: {}", 
               connection.getId(), e.getMessage());
        handleConnectionError(connection, e);
      }
    });
  }
  
  /**
   * Broadcast binary data to all connected clients.
   * @param data The binary data to broadcast
   */
  public void broadcastBinary(byte[] data) {
    if (connections.isEmpty()) {
      return;
    }
    
    connections.forEach(connection -> {
      try {
        connection.sendBinary(data);
      } catch (IOException e) {
        logger.warn("Failed to send broadcast binary data to {}: {}", connection.getId(), e.getMessage());
        handleConnectionError(connection, e);
      }
    });
  }
  
  /**
   * Handle connection errors by removing the connection and calling the error callback.
   * @param connection The connection that encountered an error
   * @param error The error that occurred
   */
  private void handleConnectionError(WebSocketConnection connection, Exception error) {
    connections.remove(connection);
    onError(connection, error);
    try {
      connection.getChannel().close();
    } catch (IOException e) {
      // Ignore close errors
    }
  }

  /**
   * Start the WebSocket server on a new thread.
   */
  public void start() {
    if (running) {
      logger.warn("Server is already running");
      return;
    }
    
    Thread serverThread = new Thread(this::run, "WebSocket-Server");
    serverThread.setDaemon(false);
    serverThread.start();
    
    logger.info("WebSocket server starting on port {}", port);
  }
  
  /**
   * Stop the WebSocket server gracefully.
   */
  public void stop() {
    if (!running) {
      return;
    }
    
    running = false;
    logger.info("Stopping WebSocket server...");
    
    try {
      // Close all client connections gracefully
      closeAllConnections(CLOSE_GOING_AWAY, "Server shutting down");
      
      if (selector != null) {
        selector.wakeup();
      }
      if (serverChannel != null) {
        serverChannel.close();
      }
      
      // Shutdown executor service
      if (executorService != null) {
        executorService.shutdown();
        try {
          if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
          }
        } catch (InterruptedException e) {
          executorService.shutdownNow();
          Thread.currentThread().interrupt();
        }
      }
      
      logger.info("WebSocket server stopped");
    } catch (IOException e) {
      logger.error("Error stopping WebSocket server", e);
    }
  }
  
  /**
   * Get the number of connected clients.
   * @return The number of active connections
   */
  public int getConnectionCount() {
    return connections.size();
  }
  
  /**
   * Get all active connections (read-only view).
   * @return A set of all active connections
   */
  public Set<WebSocketConnection> getConnections() {
    return Set.copyOf(connections);
  }
  
  /**
   * Close all connections with the specified status code and reason.
   * @param statusCode The close status code
   * @param reason The close reason
   */
  private void closeAllConnections(int statusCode, String reason) {
    connections.forEach(connection -> {
      try {
        connection.close(statusCode, reason);
      } catch (IOException e) {
        logger.warn("Error closing connection {}: {}", connection.getId(), e.getMessage());
      }
    });
    connections.clear();
  }
  
  /**
   * Main server loop - runs in a separate thread.
   */
  private void run() {
    try {
      initialiseServer();
      while (running) {
        handleServerEvents();
      }
    } catch (IOException e) {
      logger.error("WebSocket server error", e);
    } finally {
      cleanup();
    }
  }
  
  /**
   * Initialise the server socket and selector.
   * @throws IOException If an I/O error occurs
   */
  private void initialiseServer() throws IOException {
    serverChannel = ServerSocketChannel.open();
    serverChannel.configureBlocking(false);
    serverChannel.bind(new InetSocketAddress(port));
    
    selector = Selector.open();
    serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    
    running = true;
    logger.info("WebSocket server started on port {} (max frame size: {} bytes)", port, maxFrameSize);
  }
  
  /**
   * Handle server events (accept, read, write).
   * @throws IOException If an I/O error occurs
   */
  private void handleServerEvents() throws IOException {
    selector.select(1000); // 1 second timeout
    
    Set<SelectionKey> selectedKeys = selector.selectedKeys();
    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
    
    while (keyIterator.hasNext()) {
      SelectionKey key = keyIterator.next();
      keyIterator.remove();
      
      if (!key.isValid()) {
        continue;
      }
      
      try {
        if (key.isAcceptable()) {
          acceptConnection();
        } else if (key.isReadable()) {
          handleRead(key);
        }
      } catch (Exception e) {
        logger.warn("Error handling server event", e);
        if (key.channel() instanceof SocketChannel) {
          handleChannelError((SocketChannel) key.channel(), e);
        }
      }
    }
  }
  
  /**
   * Accept a new client connection.
   * @throws IOException If an I/O error occurs
   */
  private void acceptConnection() throws IOException {
    SocketChannel clientChannel = serverChannel.accept();
    if (clientChannel != null) {
      clientChannel.configureBlocking(false);
      clientChannel.register(selector, SelectionKey.OP_READ);
      logger.debug("New client connection accepted from {}", clientChannel.getRemoteAddress());
    }
  }
  
  /**
   * Handle read events from client channels.
   * @param key The selection key for the readable channel
   */
  private void handleRead(SelectionKey key) {
    SocketChannel clientChannel = (SocketChannel) key.channel();
    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    
    try {
      int bytesRead = clientChannel.read(buffer);
      if (bytesRead == -1) {
        // Client disconnected
        handleClientDisconnection(clientChannel, CLOSE_NORMAL, "Client disconnected");
        key.cancel();
        return;
      }
      
      if (bytesRead == 0) {
        return; // No data available
      }
      
      buffer.flip();
      byte[] data = new byte[buffer.limit()];
      buffer.get(data);
      
      WebSocketConnection connection = findConnectionByChannel(clientChannel);
      
      if (connection == null && isWebSocketHandshake(new String(data, "UTF-8"))) {
        // Handle new WebSocket handshake
        handleWebSocketHandshake(clientChannel, new String(data, "UTF-8"));
      } else if (connection != null) {
        // Handle WebSocket frame
        handleWebSocketFrame(connection, data);
      } else {
        // Invalid data from non-WebSocket client
        logger.warn("Received invalid data from non-WebSocket client: {}", clientChannel.getRemoteAddress());
        clientChannel.close();
        key.cancel();
      }
    } catch (Exception e) {
      logger.warn("Error reading from client {}: {}", getChannelAddress(clientChannel), e.getMessage());
      handleChannelError(clientChannel, e);
      key.cancel();
    }
  }
  
  /**
   * Find a WebSocket connection by its underlying channel.
   * @param channel The socket channel to find
   * @return The WebSocket connection, or null if not found
   */
  private WebSocketConnection findConnectionByChannel(SocketChannel channel) {
    return connections.stream()
        .filter(conn -> conn.getChannel().equals(channel))
        .findFirst()
        .orElse(null);
  }
  
  /**
   * Handle WebSocket handshake for a new client.
   * @param clientChannel The client socket channel
   * @param request The HTTP request containing the handshake
   * @throws IOException If an I/O error occurs
   */
  private void handleWebSocketHandshake(SocketChannel clientChannel, String request) throws IOException {
    try {
      performHandshake(clientChannel, request);
      WebSocketConnection connection = new WebSocketConnection(clientChannel);
      connections.add(connection);
      onOpen(connection);
      logger.info("WebSocket handshake completed for client: {}", connection.getId());
    } catch (Exception e) {
      logger.warn("WebSocket handshake failed for {}: {}", getChannelAddress(clientChannel), e.getMessage());
      clientChannel.close();
      throw e;
    }
  }
  
  /**
   * Handle WebSocket frame data from a client.
   * @param connection The WebSocket connection
   * @param data The raw frame data
   */
  private void handleWebSocketFrame(WebSocketConnection connection, byte[] data) {
    try {
      WebSocketFrame frame = parseWebSocketFrame(data);
      if (frame == null) {
        logger.warn("Invalid WebSocket frame from {}", connection.getId());
        return;
      }
      
      switch (frame.opcode) {
        case OPCODE_TEXT:
          onMessage(connection, frame.getTextPayload());
          break;
        case OPCODE_BINARY:
          onBinaryMessage(connection, frame.payload);
          break;
        case OPCODE_CLOSE:
          int statusCode = frame.getCloseStatusCode();
          String reason = frame.getCloseReason();
          logger.info("Received close frame from {}: {} - {}", connection.getId(), statusCode, reason);
          handleClientDisconnection(connection.getChannel(), statusCode, reason);
          break;
        case OPCODE_PING:
          onPing(connection, frame.payload);
          break;
        case OPCODE_PONG:
          onPong(connection, frame.payload);
          break;
        default:
          logger.warn("Unsupported WebSocket frame opcode: 0x{} from {}", Integer.toHexString(frame.opcode), connection.getId());
          break;
      }
    } catch (Exception e) {
      logger.warn("Error handling WebSocket frame from {}: {}", connection.getId(), e.getMessage());
      handleConnectionError(connection, e);
    }
  }
  
  /**
   * Handle client disconnection.
   * @param clientChannel The client channel that disconnected
   * @param statusCode The close status code
   * @param reason The close reason
   */
  private void handleClientDisconnection(SocketChannel clientChannel, int statusCode, String reason) {
    WebSocketConnection connection = findConnectionByChannel(clientChannel);
    if (connection != null) {
      connections.remove(connection);
      onClose(connection, statusCode, reason);
    }
    
    try {
      clientChannel.close();
    } catch (IOException e) {
      // Ignore close errors
    }
  }
  
  /**
   * Handle errors with a specific channel.
   * @param clientChannel The channel that encountered an error
   * @param error The error that occurred
   */
  private void handleChannelError(SocketChannel clientChannel, Exception error) {
    WebSocketConnection connection = findConnectionByChannel(clientChannel);
    if (connection != null) {
      handleConnectionError(connection, error);
    } else {
      try {
        clientChannel.close();
      } catch (IOException e) {
        // Ignore close errors
      }
    }
  }
  
  /**
   * Get the string representation of a channel's address.
   * @param channel The socket channel
   * @return The address string, or "unknown" if unavailable
   */
  private String getChannelAddress(SocketChannel channel) {
    try {
      return channel.getRemoteAddress().toString();
    } catch (IOException e) {
      return "unknown";
    }
  }
  
  private boolean isWebSocketHandshake(String request) {
    return request.contains("Upgrade: websocket") && 
      request.contains("Connection: Upgrade") &&
      request.contains("Sec-WebSocket-Key:");
  }
  
  private void performHandshake(SocketChannel clientChannel, String request) throws IOException {
    Matcher matcher = handshakePattern.matcher(request);
    
    if (!matcher.find()) {
      throw new IOException("Invalid WebSocket handshake");
    }
    
    String key = matcher.group(1).trim();
    String acceptKey = generateAcceptKey(key);
    
    String response = "HTTP/1.1 101 Switching Protocols\r\n" +
        "Upgrade: websocket\r\n" +
        "Connection: Upgrade\r\n" +
        "Sec-WebSocket-Accept: " + acceptKey + "\r\n\r\n";
    
    ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
    clientChannel.write(responseBuffer);
  }
    
  private String generateAcceptKey(String key) {
    try {
      String combined = key + WEBSOCKET_MAGIC_STRING;
      MessageDigest digest = MessageDigest.getInstance("SHA-1");
      byte[] hash = digest.digest(combined.getBytes());
      return Base64.getEncoder().encodeToString(hash);
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate WebSocket accept key", e);
    }
  }
  
  private WebSocketFrame parseWebSocketFrame(byte[] data) {
    if (data.length < 2) {
      return null;
    }
    
    byte firstByte = data[0];
    byte opcode = (byte) (firstByte & 0x0F);
    
    int payloadStart = 2;
    int payloadLength = data[1] & 0x7F;
    boolean masked = (data[1] & 0x80) != 0;

    byte rsv = (byte) ((firstByte >> 4) & 0x07);
    if (rsv != 0) {
      // Protocol error - reserved bits must be 0
      return null;
    }
    
    if (payloadLength == 126) {
      if (data.length < 4) return null;
      payloadLength = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
      payloadStart = 4;
    } else if (payloadLength == 127) {
      if (data.length < 10) return null;
      long fullLength = 0;
      for (int i = 2; i < 10; i++) {
        fullLength = (fullLength << 8) | (data[i] & 0xFF);
      }
      payloadLength = (int) Math.min(fullLength, Integer.MAX_VALUE);
      payloadStart = 10;
    }

    if ((opcode & 0x8) != 0 && payloadLength > 125) {
      // Protocol error - control frame too large
      return null;
    }
    
    if (masked) {
      payloadStart += 4; // Skip mask key
    }
    
    if (data.length < payloadStart + payloadLength) {
      return null;
    }
    
    byte[] payload = new byte[payloadLength];
    System.arraycopy(data, payloadStart, payload, 0, payloadLength);
    
    if (masked) {
      byte[] mask = new byte[4];
      System.arraycopy(data, payloadStart - 4, mask, 0, 4);
      
      for (int i = 0; i < payloadLength; i++) {
      payload[i] ^= mask[i % 4];
      }
    }
    
    return new WebSocketFrame(opcode, payload);
  }
  
  /**
   * Inner class to represent a parsed WebSocket frame.
   */
  private static class WebSocketFrame {
    final byte opcode;
    final byte[] payload;
    
    WebSocketFrame(byte opcode, byte[] payload) {
      this.opcode = opcode;
      this.payload = payload;
    }
    
    String getTextPayload() {
      return new String(payload);
    }
    
    int getCloseStatusCode() {
      if (payload.length >= 2) {
        int code = ((payload[0] & 0xFF) << 8) | (payload[1] & 0xFF);
        // Validate code is in allowed ranges
        if (code < 1000 || code == 1004 || code == 1005 || 
          code == 1006 || (code >= 1012 && code <= 1014) || 
          code == 1015 || (code >= 1016 && code <= 1999)) {
          return CLOSE_PROTOCOL_ERROR;
        }
        return code;
      }
      return CLOSE_NO_STATUS;
    }
    
    String getCloseReason() {
      if (payload.length > 2) {
        return new String(payload, 2, payload.length - 2);
      }
      return "";
    }
  }
  
  /**
   * Clean up server resources.
   */
  private void cleanup() {
    try {
      // Close all connections gracefully
      closeAllConnections(CLOSE_GOING_AWAY, "Server shutting down");
      
      if (selector != null) {
        selector.close();
      }
      if (serverChannel != null) {
        serverChannel.close();
      }
    } catch (IOException e) {
      logger.error("Error during cleanup", e);
    }
  }
}
