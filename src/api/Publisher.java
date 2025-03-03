package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * A class to publish messages to a Redis channel through a given Jedis instance.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Publisher {
  // The logger to log messages.
  private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

  private final Jedis publisherJedis; // The Jedis instance to publish messages.
  private final String channel; // The channel to publish messages to.

  /**
   * Constructor -- Create a new Publisher object.
   */
  public Publisher(Jedis publisherJedis, String channel) {
    this.publisherJedis = publisherJedis;
    this.channel = channel;
  }

  /**
   * Publish the given message to the channel.
   * @param message The message to publish.
   */
  public void publish(String message) {
    String formattedMessage = message.length() > 10 ? message.substring(0, 10) : message;
    logger.info("Publishing on channel {} the message: {}...", channel, formattedMessage);
    publisherJedis.publish(channel, message);
  }
}