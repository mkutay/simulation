package graphics.methods;

public class DrawRect extends Method {
  public int x;
  public int y;
  public int w;
  public int h;
  public boolean filled;

  public DrawRect(String name, int x, int y, int w, int h, int[] color, boolean filled) {
    super(name, color);
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.filled = filled;
  }
}
