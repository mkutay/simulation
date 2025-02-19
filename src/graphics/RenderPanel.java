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
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class RenderPanel {
	private DisplayData data;
	private JedisPooled client;

	public RenderPanel(int width, int height) {
		data = new DisplayData(width, height);
		Dotenv dotenv = Dotenv.load();
		String redisUri = dotenv.get("REDIS_URI");
		URI uri = URI.create(redisUri);
		client = new JedisPooled(uri);
	}

	public void fill(Color color) {
		data.add(new Fill("fill", getArrayFromColor(color)));
	}

	public void drawCircle(int x, int y, int radius, Color color) {
		data.add(new DrawCircle("drawCircle", x, y, radius, getArrayFromColor(color)));
	}

	public void drawRect(int x, int y, int width, int height, Color color, boolean filled) {
		data.add(new DrawRect("drawRect", x, y, width, height, getArrayFromColor(color), filled));
	}

	public void drawEqualTriangle(int centerX, int centerY, int radius, Color color) {
		data.add(new DrawEqualTriangle("drawEqualTriangle", centerX, centerY, radius, getArrayFromColor(color)));
	}

	public void drawText(String text, int fontSize, int x, int y, Color color) {
		data.add(new DrawText("drawText", text, fontSize, x, y, getArrayFromColor(color)));
	}

	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		data.add(new DrawLine("drawLine", x1, y1, x2, y2, getArrayFromColor(color)));
	}

	public void drawTransparentRect(int x, int y, int width, int height, Color color, double alpha) {
		data.add(new DrawTransparentRect("drawTransparentRect", x, y, width, height, getArrayFromColor(color), alpha));
	}

	// Send all the stored data to the redis database.
	public void update() {
		Gson g = new Gson();
		String j = g.toJson(data);

		client.jsonSet("display", j);

		data.d.clear();
	}

	private int[] getArrayFromColor(Color c) {
		return new int[] { c.getRed(), c.getGreen(), c.getBlue() };
	}

	public int getWidth() { return data.w; }
	public int getHeight() { return data.h; }
}