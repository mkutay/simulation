package graphics;

import java.awt.Color;
import java.net.URI;

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
	private DisplayData data; // The data to be sent to the Redis database.
	private JedisPooled client; // The Redis client.
	private int index = 0; // The global index of the data.

	/**
	 * Constructor -- Create a new RenderPanelWeb object.
	 * @param width The width of the display.
	 * @param height The height of the display.
	 */
	public RenderPanelWeb(int width, int height) {
		data = new DisplayData(width, height);
		Dotenv dotenv = Dotenv.load();
		String redisUri = dotenv.get("REDIS_URI");
		URI uri = URI.create(redisUri);
		client = new JedisPooled(uri);
	}

	public void fill(Color color) {
		Method m = data.get("f", new Fill());
		((Fill) m).add(index++, getArrayFromColor(color));
	}

	public void drawCircle(int x, int y, int radius, Color color) {
		Method m = data.get("c", new DrawCircle());
		((DrawCircle) m).add(index++, x, y, radius, getArrayFromColor(color));
	}

	public void drawRect(int x, int y, int width, int height, Color color, boolean filled) {
		Method m = data.get("r", new DrawRect());
		((DrawRect) m).add(index++, x, y, width, height, getArrayFromColor(color), filled);
	}

	public void drawEqualTriangle(int centerX, int centerY, int radius, Color color) {
		Method m = data.get("e", new DrawEqualTriangle());
		((DrawEqualTriangle) m).add(index++, centerX, centerY, radius, getArrayFromColor(color));
	}
  
	public void drawText(String text, int fontSize, int x, int y, Color color) {
		Method m = data.get("t", new DrawText());
		((DrawText) m).add(index++, text, fontSize, x, y, getArrayFromColor(color));
	}
  
	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		Method m = data.get("l", new DrawLine());
		((DrawLine) m).add(index++, x1, y1, x2, y2, getArrayFromColor(color));
	}

	public void drawTransparentRect(int x, int y, int width, int height, Color color, double alpha) {
		Method m = data.get("a", new DrawTransparentRect());
		((DrawTransparentRect) m).add(index++, x, y, width, height, getArrayFromColor(color), alpha);
	}

	/**
	 * Send all the stored data to the redis database.
	 */
	public void update() {
		for (String key : data.d.keySet()) {
			data.d.get(key).reverse();
		}

		Gson g = new Gson();
		String j = g.toJson(data);

		client.jsonSet("display", j);
		data.d.clear();
		index = 0;
	}

	/**
	 * Get the three-wide array from a Color object.
	 */
	private int[] getArrayFromColor(Color c) {
		return new int[] { c.getRed(), c.getGreen(), c.getBlue() };
	}
}