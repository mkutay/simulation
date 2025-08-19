package api;

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
public class WebSocketMessageHandler {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageHandler.class);
  
  private Engine engine;
  
  /**
   * Handle incoming WebSocket message for simulation control.
   * @param message The message received from the WebSocket client.
   */
  public void handleMessage(String message) {
    String formattedMessage = message.length() > 50 ? message.substring(0, 50) : message;
    logger.info("Message received: {}...", formattedMessage);

    Gson g = new Gson();
    APISchema schema = g.fromJson(message, APISchema.class);

    if (schema.type.equals("stop_simulation")) {
      stopEngine();
      return;
    }

    if (!schema.type.equals("start_simulation")) {
      logger.warn("Unknown message type: {}", schema.type);
      return;
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
    engine = new Engine(600, 600, 60);
    engine.start();
  }
  
  private void stopEngine() {
    if (engine != null) {
      engine.stop();
    }
  }

  private static class APISchema {
    public String type;
    public String data;
  }
}
