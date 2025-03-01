package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;
import simulation.simulationData.Data;
import simulation.simulationData.SimulationData;
import util.Parser;
import view.Engine;

/**
 * A class to subscribe to the Redis channel and start the simulation when a message is received.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Subscriber extends JedisPubSub {
  private static Logger logger = LoggerFactory.getLogger(Subscriber.class); // The logger to log messages.

  private Engine engine; // The engine that is running the simulation.

  /**
   * Called when a message is received on the channel. Start the simulation with the received data.
   * If the message is empty, stop the simulation.
   * @param channel The channel the message was received on.
   * @param message The message that was received.
   */
  @Override
  public void onMessage(String channel, String message) {
    String formattedMessage = message.length() > 10 ? message.substring(0, 10) : message;
    logger.info("Message received. Channel: {}, Message: {}...", channel, formattedMessage);

    if (message.equals("")) { // Stop the simulation
      stopEngine();
      return;
    }

    stopEngine();

    // Get and set the simulation data:
    SimulationData data = null;
    try {
      data = Parser.parseSimulationData(message);
    } catch (Exception e) {
      logger.error("Failed to parse the simulation data.", e);
      return;
    }
    Data.setSimulationData(data);

    // Start the simulation:
    engine = new Engine(600, 600, 60);
    engine.start();
  }

  @Override
  public void onSubscribe(String channel, int subscribedChannels) {
    logger.info("Someone subscribed to \"{}\". Subscribed channels: {}", channel, subscribedChannels);
  }

  @Override
  public void onUnsubscribe(String channel, int subscribedChannels) {
    logger.info("Someone unsubscribed from \"{}\". Subscribed channels: {}", channel, subscribedChannels);
  }

  @Override
  public void onPMessage(String pattern, String channel, String message) { }

  @Override
  public void onPUnsubscribe(String pattern, int subscribedChannels) { }

  @Override
  public void onPSubscribe(String pattern, int subscribedChannels) { }

  private void stopEngine() {
    if (engine != null) {
      engine.stop();
    }
  }
}
