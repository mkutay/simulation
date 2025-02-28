package api;

import java.net.URI;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Connector {
  public static final String CHANNEL_NAME = "simulation_data";
  private static Logger logger = LoggerFactory.getLogger(Connector.class);

  private final JedisPool jedisPool;
  private final Jedis subscriberJedis;
  private final Subscriber subscriber;

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

  public void listen() {
    try {
      logger.info("Subscribing to \"{}\". This thread will be blocked.", CHANNEL_NAME);
      subscriberJedis.subscribe(subscriber, CHANNEL_NAME);
      logger.info("Subscription ended.");
    } catch (Exception e) {
      logger.error("Subscribing failed.", e);
    }
  }

  public void start() {
    Thread t = new Thread(this::listen);
    t.start();
  }

  public void close() {
    subscriber.unsubscribe();
    jedisPool.returnResource(subscriberJedis);

    jedisPool.close();
  }
}
