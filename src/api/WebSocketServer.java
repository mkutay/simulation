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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A WebSocket server implementation that can both broadcast messages to connected clients
 * and handle incoming control messages.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class WebSocketServer {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
  private static final String WEBSOCKET_MAGIC_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
  
  private final int port;
  private ServerSocketChannel serverChannel;
  private Selector selector;
  private volatile boolean running = false;
  private final Set<SocketChannel> clients = ConcurrentHashMap.newKeySet();
  private final WebSocketMessageHandler messageHandler;
  
  public WebSocketServer(int port) {
    this.port = port;
    this.messageHandler = new WebSocketMessageHandler();
  }
  
  /**
   * Start the WebSocket server on a new thread.
   */
  public void start() {
    Thread serverThread = new Thread(this::run);
    serverThread.start();
  }
  
  /**
   * Stop the WebSocket server.
   */
  public void stop() {
    running = false;
    try {
      if (selector != null) {
        selector.wakeup();
      }
      if (serverChannel != null) {
        serverChannel.close();
      }
    } catch (IOException e) {
      logger.error("Error stopping WebSocket server", e);
    }
  }
  
  /**
   * Broadcast a message to all connected clients.
   */
  public void broadcast(String message) {
    if (clients.isEmpty()) {
      return;
    }
    
    byte[] messageBytes = message.getBytes();
    ByteBuffer frame = createWebSocketFrame(messageBytes);
    
    Iterator<SocketChannel> iterator = clients.iterator();
    while (iterator.hasNext()) {
      SocketChannel client = iterator.next();
      try {
        frame.rewind();
        client.write(frame);
      } catch (IOException e) {
        logger.warn("Failed to send message to client, removing", e);
        iterator.remove();
        try {
          client.close();
        } catch (IOException ex) {
          // Ignore
        }
      }
    }
  }
  
  private void run() {
    try {
      serverChannel = ServerSocketChannel.open();
      serverChannel.configureBlocking(false);
      serverChannel.bind(new InetSocketAddress(port));
      
      selector = Selector.open();
      serverChannel.register(selector, SelectionKey.OP_ACCEPT);
      
      running = true;
      logger.info("WebSocket server started on port {}", port);
      
      while (running) {
        selector.select(1000);
        
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        
        while (keyIterator.hasNext()) {
          SelectionKey key = keyIterator.next();
          keyIterator.remove();
          
          if (!key.isValid()) {
            continue;
          }
          
          if (key.isAcceptable()) {
            acceptConnection();
          } else if (key.isReadable()) {
            handleRead(key);
          }
        }
      }
    } catch (IOException e) {
      logger.error("WebSocket server error", e);
    } finally {
      cleanup();
    }
  }
  
  private void acceptConnection() {
    try {
      SocketChannel clientChannel = serverChannel.accept();
      if (clientChannel != null) {
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        logger.info("New client connected");
      }
    } catch (IOException e) {
      logger.error("Error accepting connection", e);
    }
  }
  
  private void handleRead(SelectionKey key) {
    SocketChannel clientChannel = (SocketChannel) key.channel();
    ByteBuffer buffer = ByteBuffer.allocate(8192);
    
    try {
      int bytesRead = clientChannel.read(buffer);
      if (bytesRead == -1) {
        // Client disconnected
        clients.remove(clientChannel);
        clientChannel.close();
        key.cancel();
        logger.info("Client disconnected");
        return;
      }
      
      buffer.flip();
      byte[] data = new byte[buffer.limit()];
      buffer.get(data);
      
      if (isWebSocketHandshake(new String(data))) {
        performHandshake(clientChannel, new String(data));
        clients.add(clientChannel);
        logger.info("WebSocket handshake completed, client added");
      } else if (clients.contains(clientChannel)) {
        // Handle WebSocket frame
        String message = parseWebSocketFrame(data);
        if (message != null) {
          messageHandler.handleMessage(message);
        }
      }
      
    } catch (Exception e) {
      logger.warn("Error reading from client", e);
      clients.remove(clientChannel);
      try {
        clientChannel.close();
      } catch (IOException ex) {
        // Ignore
      }
      key.cancel();
    } finally {
      if (clients.isEmpty()) {
        logger.info("No clients connected, stopping");
        messageHandler.stopEngine();
      }
    }
  }
  
  private boolean isWebSocketHandshake(String request) {
    return request.contains("Upgrade: websocket") && 
         request.contains("Connection: Upgrade") &&
         request.contains("Sec-WebSocket-Key:");
  }
  
  private void performHandshake(SocketChannel clientChannel, String request) throws IOException {
    Pattern pattern = Pattern.compile("Sec-WebSocket-Key: (.+)");
    Matcher matcher = pattern.matcher(request);
    
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
  
  private ByteBuffer createWebSocketFrame(byte[] payload) {
    int payloadLength = payload.length;
    ByteBuffer frame;
    
    if (payloadLength < 126) {
      frame = ByteBuffer.allocate(2 + payloadLength);
      frame.put((byte) 0x81); // FIN=1, opcode=1 (text)
      frame.put((byte) payloadLength);
    } else if (payloadLength < 65536) {
      frame = ByteBuffer.allocate(4 + payloadLength);
      frame.put((byte) 0x81); // FIN=1, opcode=1 (text)
      frame.put((byte) 126);
      frame.putShort((short) payloadLength);
    } else {
      frame = ByteBuffer.allocate(10 + payloadLength);
      frame.put((byte) 0x81); // FIN=1, opcode=1 (text)
      frame.put((byte) 127);
      frame.putLong(payloadLength);
    }
    
    frame.put(payload);
    frame.flip();
    return frame;
  }
  
  private String parseWebSocketFrame(byte[] data) {
    if (data.length < 2) {
      return null;
    }
    
    int payloadStart = 2;
    int payloadLength = data[1] & 0x7F;
    boolean masked = (data[1] & 0x80) != 0;
    
    if (payloadLength == 126) {
      if (data.length < 4) return null;
      payloadLength = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
      payloadStart = 4;
    } else if (payloadLength == 127) {
      if (data.length < 10) return null;
      // For simplicity, we'll assume payloads < 2^31 bytes
      payloadLength = (int) (((long)(data[6] & 0xFF) << 24) |
                  ((long)(data[7] & 0xFF) << 16) |
                  ((long)(data[8] & 0xFF) << 8) |
                  ((long)(data[9] & 0xFF)));
      payloadStart = 10;
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
    
    return new String(payload);
  }
  
  private void cleanup() {
    try {
      for (SocketChannel client : clients) {
        client.close();
      }
      clients.clear();
      
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
  
  /**
   * Get the number of connected clients.
   */
  public int getClientCount() {
    return clients.size();
  }
}
