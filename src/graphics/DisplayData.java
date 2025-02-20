package graphics;

import java.util.ArrayList;
import java.util.List;

import graphics.methods.Method;

public class DisplayData {
  public int w;
	public int h;
	public List<Method> d;

  public DisplayData(int width, int height) {
    this.w = width;
    this.h = height;
    d = new ArrayList<>();
  }

  public void add(Method m) {
    d.add(m);
  }
}
