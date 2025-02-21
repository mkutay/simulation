package graphics.methods;

public class DrawText extends Method {
  public int x;
  public int y;
  public String t; // text
  public int s; // size

  public DrawText(int i, String text, int size, int x, int y, int[] color) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.t = text;
    this.s = size;
  }
}
