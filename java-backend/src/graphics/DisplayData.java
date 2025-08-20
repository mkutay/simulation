package graphics;

import java.util.HashMap;

import graphics.methods.Method;

/**
 * Store the display data for the web app that will be hold in the
 * Redis database.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DisplayData {
  public int w; // Width of the display.
	public int h; // Height of the display.
	public HashMap<String, Method> d; // Data of all of the function calls.

  /**
   * Constructor.
   */
  public DisplayData(int width, int height) {
    this.w = width;
    this.h = height;
    d = new HashMap<>();
  }

  /**
   * Get the method from the data.
   * @param name The name of the method.
   * @param method Used as a fail safe if the method is not found.
   * @return The method.
   */
  public Method get(String name, Method method) {
    if (d.get(name) == null) {
      d.put(name, method);
    }
    return d.get(name);
  }
}