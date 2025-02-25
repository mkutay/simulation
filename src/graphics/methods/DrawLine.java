package graphics.methods;

/**
 * Method to "draw a line" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawLine extends Method {
  public int x1; // X-coordinate of the first point.
  public int y1; // Y-coordinate of the first point.
  public int x2; // X-coordinate of the second point.
  public int y2; // Y-coordinate of the second point.

  /**
   * Constructor.
   */
  public DrawLine(int i, int x1, int y1, int x2, int y2, int[] color) {
    super(i, color);
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }
}
