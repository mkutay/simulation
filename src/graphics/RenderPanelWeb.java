package graphics;

import java.awt.Color;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import redis.clients.jedis.JedisPooled;

import graphics.methods.*;

/**
 * Send the simulation data to the Redis database to update the web app.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class RenderPanelWeb implements RenderPanel {
	private DisplayData data;
	private JedisPooled client;
	private int index = 0;

	public RenderPanelWeb(int width, int height) {
		data = new DisplayData(width, height);
		Dotenv dotenv = Dotenv.load();
		String redisUri = dotenv.get("REDIS_URI");
		URI uri = URI.create(redisUri);
		client = new JedisPooled(uri);
	}

	public void fill(Color color) {
		data.add("f", new Fill(index++, getArrayFromColor(color)));
	}

	public void drawCircle(int x, int y, int radius, Color color) {
		data.add("c", new DrawCircle(index++, x, y, radius, getArrayFromColor(color)));
	}

	public void drawRect(int x, int y, int width, int height, Color color, boolean filled) {
		data.add("r", new DrawRect(index++, x, y, width, height, getArrayFromColor(color), filled));
	}

	public void drawEqualTriangle(int centerX, int centerY, int radius, Color color) {
		data.add("e", new DrawEqualTriangle(index++, centerX, centerY, radius, getArrayFromColor(color)));
	}
  
	public void drawText(String text, int fontSize, int x, int y, Color color) {
		data.add("t", new DrawText(index++, text, fontSize, x, y, getArrayFromColor(color)));
	}
  
	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		data.add("l", new DrawLine(index++, x1, y1, x2, y2, getArrayFromColor(color)));
	}

	public void drawTransparentRect(int x, int y, int width, int height, Color color, double alpha) {
		data.add("a", new DrawTransparentRect(index++, x, y, width, height, getArrayFromColor(color), alpha));
	}

	/**
	 * Send all the stored data to the redis database.
	 */
	public void update() {
		HashMap<String, List<Method>> d = new HashMap<>();
		for (String key : data.d.keySet()) {
			d.put(key, data.d.get(key).reversed());
		}
		data.d = d;

		Gson g = new Gson();
		String j = g.toJson(data);

		client.jsonSet("display", j);
		data.d.clear();
		index = 0;
	}

	private int[] getArrayFromColor(Color c) {
		return new int[] { c.getRed(), c.getGreen(), c.getBlue() };
	}
}