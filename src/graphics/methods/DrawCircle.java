package graphics.methods;

/**
 * Method to "draw a circle" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawCircle extends Method {
  public int x; // X-coordinate of the circle.
  public int y; // Y-coordinate of the circle.
  public int r; // Radius of the circle.

  /**
   * Constructor.
   */
  public DrawCircle(int i, int x, int y, int r, int[] color) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.r = r;
  }
}
