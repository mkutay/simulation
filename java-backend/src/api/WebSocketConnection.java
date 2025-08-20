package api;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a WebSocket connection to a client.
 * Provides methods for sending different types of WebSocket frames
 * and managing connection state.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class WebSocketConnection {
  private final SocketChannel channel;
  private final String id;
  private final long connectTime;
  private final AtomicLong lastPingTime = new AtomicLong(0);
  private final AtomicLong lastPongTime = new AtomicLong(System.currentTimeMillis());
  private volatile boolean open = true;
  
  /**
   * Create a new WebSocket connection wrapper.
   * @param channel The underlying socket channel
   */
  public WebSocketConnection(SocketChannel channel) {
    this.channel = channel;
    this.connectTime = System.currentTimeMillis();
    this.id = generateConnectionId();
  }
  
  /**
   * Get the unique connection ID.
   * @return The connection ID
   */
  public String getId() {
    return id;
  }
  
  /**
   * Get the underlying socket channel.
   * @return The socket channel
   */
  public SocketChannel getChannel() {
    return channel;
  }
  
  /**
   * Get the remote address of the client.
   * @return The remote socket address
   */
  public SocketAddress getRemoteAddress() {
    try {
      return channel.getRemoteAddress();
    } catch (IOException e) {
      return null;
    }
  }
  
  /**
   * Check if the connection is open.
   * @return true if the connection is open, false otherwise
   */
  public boolean isOpen() {
    return open && channel.isOpen() && channel.isConnected();
  }
  
  /**
   * Get the connection time.
   * @return The time when the connection was established
   */
  public long getConnectTime() {
    return connectTime;
  }
  
  /**
   * Get the last ping time.
   * @return The time when the last ping was sent
   */
  public long getLastPingTime() {
    return lastPingTime.get();
  }
  
  /**
   * Get the last pong time.
   * @return The time when the last pong was received
   */
  public long getLastPongTime() {
    return lastPongTime.get();
  }
  
  /**
   * Update the last pong time to current time.
   */
  public void updateLastPongTime() {
    lastPongTime.set(System.currentTimeMillis());
  }
  
  /**
   * Send a text message to the client.
   * @param message The text message to send
   * @throws IOException If an I/O error occurs
   */
  public void sendText(String message) throws IOException {
    if (!isOpen()) {
      throw new IOException("Connection is closed");
    }
    sendFrame(message.getBytes("UTF-8"), WebSocketServer.OPCODE_TEXT);
  }
  
  /**
   * Send binary data to the client.
   * @param data The binary data to send
   * @throws IOException If an I/O error occurs
   */
  public void sendBinary(byte[] data) throws IOException {
    if (!isOpen()) {
      throw new IOException("Connection is closed");
    }
    sendFrame(data, WebSocketServer.OPCODE_BINARY);
  }
  
  /**
   * Send a ping frame to the client.
   * @param payload The ping payload (optional, can be null)
   * @throws IOException If an I/O error occurs
   */
  public void sendPing(byte[] payload) throws IOException {
    if (!isOpen()) {
      throw new IOException("Connection is closed");
    }
    byte[] data = payload != null ? payload : new byte[0];
    sendFrame(data, WebSocketServer.OPCODE_PING);
    lastPingTime.set(System.currentTimeMillis());
  }
  
  /**
   * Send a pong frame to the client.
   * @param payload The pong payload (should match the ping payload)
   * @throws IOException If an I/O error occurs
   */
  public void sendPong(byte[] payload) throws IOException {
    if (!isOpen()) {
      throw new IOException("Connection is closed");
    }
    byte[] data = payload != null ? payload : new byte[0];
    sendFrame(data, WebSocketServer.OPCODE_PONG);
  }
  
  /**
   * Send a close frame and close the connection.
   * @param statusCode The close status code
   * @param reason The close reason
   * @throws IOException If an I/O error occurs
   */
  public void close(int statusCode, String reason) throws IOException {
    if (!isOpen()) {
      return;
    }
    
    open = false;
    
    try {
      // Create close frame payload
      byte[] reasonBytes = reason != null ? reason.getBytes("UTF-8") : new byte[0];
      byte[] payload = new byte[2 + reasonBytes.length];
      payload[0] = (byte) ((statusCode >> 8) & 0xFF);
      payload[1] = (byte) (statusCode & 0xFF);
      System.arraycopy(reasonBytes, 0, payload, 2, reasonBytes.length);
      
      sendFrame(payload, WebSocketServer.OPCODE_CLOSE);
    } finally {
      channel.close();
    }
  }
  
  /**
   * Close the connection with default status code.
   * @throws IOException If an I/O error occurs
   */
  public void close() throws IOException {
    close(WebSocketServer.CLOSE_NORMAL, "Normal closure");
  }
  
  /**
   * Send a WebSocket frame with the specified payload and opcode.
   * @param payload The frame payload
   * @param opcode The frame opcode
   * @throws IOException If an I/O error occurs
   */
  private void sendFrame(byte[] payload, byte opcode) throws IOException {
    ByteBuffer frame = createWebSocketFrame(payload, opcode);
    synchronized (channel) {
      while (frame.hasRemaining()) {
        channel.write(frame);
      }
    }
  }
  
  /**
   * Create a WebSocket frame with the specified payload and opcode.
   * Supports all payload sizes as per RFC 6455.
   * @param payload The frame payload
   * @param opcode The frame opcode
   * @return The complete WebSocket frame as a ByteBuffer
   */
  private ByteBuffer createWebSocketFrame(byte[] payload, byte opcode) {
    long payloadLength = payload.length;
    ByteBuffer frame;
    
    byte firstByte = (byte) (0x80 | opcode); // FIN=1, RSV=000, opcode
    
    if (payloadLength < 126) {
      // Payload length fits in 7 bits
      frame = ByteBuffer.allocate(2 + payload.length);
      frame.put(firstByte);
      frame.put((byte) payloadLength);
    } else if (payloadLength < 65536) {
      // Payload length fits in 16 bits
      frame = ByteBuffer.allocate(4 + payload.length);
      frame.put(firstByte);
      frame.put((byte) 126);
      frame.putShort((short) payloadLength);
    } else {
      // Payload length requires 64 bits
      frame = ByteBuffer.allocate(10 + payload.length);
      frame.put(firstByte);
      frame.put((byte) 127);
      frame.putLong(payloadLength);
    }
    
    frame.put(payload);
    frame.flip();
    return frame;
  }
  
  /**
   * Generate a unique connection ID.
   * @return A unique connection identifier
   */
  private String generateConnectionId() {
    return String.format("ws-%d-%d", 
               System.currentTimeMillis(), 
               System.identityHashCode(this));
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    WebSocketConnection that = (WebSocketConnection) obj;
    return id.equals(that.id);
  }
  
  @Override
  public int hashCode() {
    return id.hashCode();
  }
  
  @Override
  public String toString() {
    return String.format("WebSocketConnection{id='%s', remote=%s, open=%s}", id, getRemoteAddress(), isOpen());
  }
}
