package graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	public HashMap<String, List<Method>> d; // Data of all of the function calls.

  /**
   * Constructor.
   */
  public DisplayData(int width, int height) {
    this.w = width;
    this.h = height;
    d = new HashMap<>();
  }

  /**
   * Add a method call to the data.
   * @param name Name of the method.
   * @param m Method to add.
   */
  public void add(String name, Method m) {
    if (!d.containsKey(name)) {
      d.put(name, new ArrayList<>());
    }
    d.get(name).add(m);
  }
}