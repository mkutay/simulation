package graphics.methods;

public class DrawCircle extends Method {
  public int x;
  public int y;
  public int r;

  public DrawCircle(int i, int x, int y, int r, int[] color) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.r = r;
  }
}
