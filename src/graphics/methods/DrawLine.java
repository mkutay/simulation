package graphics.methods;

public class DrawLine extends Method {
  public int x1;
  public int y1;
  public int x2;
  public int y2;

  public DrawLine(String name, int x1, int y1, int x2, int y2, int[] color) {
    super(name, color);
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }
}
