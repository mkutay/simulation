package graphics.methods;

/**
 * Method to "draw a transparent rectangle" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawTransparentRect extends Method {
  public int x; // X-coordinate of the rectangle.
  public int y; // Y-coordinate of the rectangle.
  public int w; // Width of the rectangle.
  public int h; // Height of the rectangle.
  public double a; // Alpha value of the rectangle.

  /**
   * Constructor.
   */
  public DrawTransparentRect(int i, int x, int y, int w, int h, int[] color, double a) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.a = a;
  }
}
