package graphics.methods;

/**
 * Method to "draw a rectangle" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawRect extends Method {
  public int x; // Top left x-coordinate of the rectangle.
  public int y; // Top left y-coordinate of the rectangle.
  public int w; // Width of the rectangle.
  public int h; // Height of the rectangle.
  public boolean f; // Whether the rectangle is filled or not.

  /**
   * Constructor.
   */
  public DrawRect(int i, int x, int y, int w, int h, int[] color, boolean filled) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.f = filled;
  }
}
