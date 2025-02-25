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
  public int w;
	public int h;
	public HashMap<String, List<Method>> d;

  public DisplayData(int width, int height) {
    this.w = width;
    this.h = height;
    d = new HashMap<>();
  }

  public void add(String name, Method m) {
    if (!d.containsKey(name)) {
      d.put(name, new ArrayList<>());
    }
    d.get(name).add(m);
  }

  public int getId(String name) {
    if (!d.containsKey(name)) {
      return 0;
    }
    return d.get(name).getLast().i;
  }
}
