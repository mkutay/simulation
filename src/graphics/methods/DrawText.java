package graphics.methods;

public class DrawText extends Method {
  public int x;
  public int y;
  public String t; // text
  public int s; // size

  public DrawText(String name, String text, int size, int x, int y, int[] color) {
    super(name, color);
    this.x = x;
    this.y = y;
    this.t = text;
    this.s = size;
  }
}
