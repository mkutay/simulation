package graphics.methods;

public class DrawRect extends Method {
  public int x;
  public int y;
  public int w;
  public int h;
  public boolean f;

  public DrawRect(int i, int x, int y, int w, int h, int[] color, boolean filled) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.f = filled;
  }
}
