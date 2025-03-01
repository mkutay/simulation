package api;

import java.net.URI;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * A class to connect to the Redis database and listen for messages.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Connector {
  // The name of the channel to listen to -- where the simulation data will be sent.
  public static final String CHANNEL_NAME = "simulation_data";

  // The logger to log messages.
  private static Logger logger = LoggerFactory.getLogger(Connector.class);

  private final JedisPool jedisPool; // The pool of Jedis connections.
  private final Jedis subscriberJedis; // The Jedis connection to subscribe to the channel.
  private final Subscriber subscriber; // The subscriber to listen for messages.

  /**
   * Constructor.
   */
  public Connector() {
    Dotenv dotenv = Dotenv.load();
		String redisUri = dotenv.get("REDIS_URI");
		URI uri = URI.create(redisUri);

    jedisPool = new JedisPool(new JedisPoolConfig(), uri, 0);
    subscriberJedis = jedisPool.getResource();
    subscriber = new Subscriber();
  }

  /**
   * Start listening for messages on the channel.
   */
  public void listen() {
    try {
      logger.info("Subscribing to \"{}\". This thread will be blocked.", CHANNEL_NAME);
      subscriberJedis.subscribe(subscriber, CHANNEL_NAME);
      logger.info("Subscription ended.");
    } catch (Exception e) {
      logger.error("Subscribing failed.", e);
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
   * Close the connection to the Redis database.
   */
  public void close() {
    subscriber.unsubscribe();
    jedisPool.returnResource(subscriberJedis);

    jedisPool.close();
  }
}
