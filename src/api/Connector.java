package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to manage WebSocket connections and handle simulation control.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Connector {
  // The port for the WebSocket server
  public static final int WEBSOCKET_PORT = 8080;

  // The logger to log messages.
  private static Logger logger = LoggerFactory.getLogger(Connector.class);

  private static Connector instance; // Singleton instance
  private final WebSocketServer webSocketServer; // The unified WebSocket server.

  /**
   * Private constructor for singleton pattern.
   */
  private Connector() {
    webSocketServer = new WebSocketServer(WEBSOCKET_PORT);
  }
  
  /**
   * Get the singleton instance.
   */
  public static synchronized Connector getInstance() {
    if (instance == null) {
      instance = new Connector();
    }
    return instance;
  }

  /**
   * Start listening for WebSocket connections and messages.
   */
  public void listen() {
    try {
      logger.info("Starting WebSocket server on port {}.", WEBSOCKET_PORT);
      webSocketServer.start();
      logger.info("WebSocket server started.");
    } catch (Exception e) {
      logger.error("WebSocket server failed.", e);
    }
  }

  /**
   * Start the listener in a new thread.
   */
  public void start() {
    Thread t = new Thread(this::listen);
    t.start();
  }

  /**
   * Close the WebSocket server.
   */
  public void close() {
    webSocketServer.stop();
  }
  
  /**
   * Get the WebSocket server instance.
   */
  public WebSocketServer getWebSocketServer() {
    return webSocketServer;
  }
}
