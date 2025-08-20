package api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

import simulation.simulationData.Data;
import simulation.simulationData.SimulationData;
import util.Parser;
import view.Engine;

/**
 * Handles WebSocket messages for simulation control.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class WebSocketHandler extends WebSocketServer {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
  
  private Map<String, Engine> engines = new ConcurrentHashMap<>();

  public WebSocketHandler(int port) {
    super(port);
  }

  @Override
  public void onOpen(WebSocketConnection connection) {
    logger.info("WebSocket connection opened: {}", connection.getId());
  }

  @Override
  public void onClose(WebSocketConnection connection, int statusCode, String reason) {
    logger.info("WebSocket connection closed: {}", connection.getId());

    Engine engine = engines.remove(connection.getId());
    
    if (engine != null) {
      logger.info("Client disconnected, stopping simulation engine");
      engine.stop();
    }

    if (getConnectionCount() == 0) {
      logger.info("No clients connected, stopping all engines.");
      for (Engine e : engines.values()) {
        e.stop();
      }
      engines.clear();
    }
  }
  
  /**
   * Handle incoming WebSocket message for simulation control.
   * @param message The message received from the WebSocket client.
   */
  @Override
  public void onMessage(WebSocketConnection connection, String message) {
    String formattedMessage = message.length() > 50 ? message.substring(0, 50) : message;
    logger.info("Message received: {}...", formattedMessage);

    Gson g = new Gson();
    APISchema schema = g.fromJson(message, APISchema.class);

    if (!schema.type.equals("start_simulation")) {
      logger.warn("Unknown message type: {}", schema.type);
      return;
    }

    Engine engine = engines.get(connection.getId());

    if (engine != null) {
      logger.warn("Simulation is already running. Stopping the current simulation before starting a new one.");
      engine.stop();
    }

    // Get and set the simulation data:
    SimulationData data = null;
    try {
      data = Parser.parseSimulationData(schema.data);
    } catch (Exception e) {
      logger.error("Failed to parse the simulation data.", e);
      return;
    }
    Data.setSimulationData(data);

    // Start the simulation:
    engine = new Engine(600, 600, 60, connection.getId());
    engines.put(connection.getId(), engine);
    engine.start();
  }

  @Override
  public void onBinaryMessage(WebSocketConnection connection, byte[] data) {
    logger.warn("Binary messages are not supported in this WebSocket handler.");
  }

  @Override
  public void onError(WebSocketConnection connection, Exception error) {
    logger.error("Error in WebSocket connection: {}", connection.getId(), error);
  }

  private static class APISchema {
    public String type;
    public String data;
  }
}
