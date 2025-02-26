package graphics.methods;

/**
 * Method to "draw an equalateral triangle" on the screen. Holds the
 * data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawEqualTriangle extends Method {
  /**
   * Add a draw equalateral triangle method call to the data.
   */
  public void add(int i, int x, int y, int r, int[] color) {
    super.add(i, color, 7);
    d.getLast()[4] = x;
    d.getLast()[5] = y;
    d.getLast()[6] = r;
  }
}
