package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;
import simulation.simulationData.Data;
import simulation.simulationData.SimulationData;
import util.Parser;
import view.Engine;

public class Subscriber extends JedisPubSub {
  private static Logger logger = LoggerFactory.getLogger(Subscriber.class);
  private Engine engine;

  @Override
  public void onMessage(String channel, String message) {
    logger.info("Message received. Channel: {}, Message: {}...", channel, message.length() > 10 ? message.substring(0, 10) : message);

    if (message.equals("")) { // Stop the simulation
      if (engine != null) {
        engine.stop();
      }
      return;
    }

    // Get and set the simulation data:
    SimulationData data = Parser.parseSimulationData(message);
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
}
