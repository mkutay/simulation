package graphics.methods;

public class DrawEqualTriangle extends Method {
  public int x;
  public int y;
  public int r;

  public DrawEqualTriangle(String name, int x, int y, int r, int[] color) {
    super(name, color);
    this.x = x;
    this.y = y;
    this.r = r;
  }
}
