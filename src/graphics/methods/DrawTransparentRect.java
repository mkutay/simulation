package graphics.methods;

public class DrawTransparentRect extends Method {
  public int x;
  public int y;
  public int w;
  public int h;
  public double a; // alpha

  public DrawTransparentRect(int i, int x, int y, int w, int h, int[] color, double a) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.a = a;
  }
}
