package graphics.methods;

/**
 * Method to "draw an equalateral triangle" on the screen. Holds the
 * data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawEqualTriangle extends Method {
  public int x; // X-coordinate of the triangle.
  public int y; // Y-coordinate of the triangle.
  public int r; // Radius of the triangle.

  /**
   * Constructor.
   */
  public DrawEqualTriangle(int i, int x, int y, int r, int[] color) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.r = r;
  }
}
