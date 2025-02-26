package graphics.methods;

/**
 * Method to "draw a rectangle" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawRect extends Method {
  /**
   * Add a draw rectangle method call to the data.
   */
  public void add(int i, int x, int y, int w, int h, int[] color, boolean filled) {
    super.add(i, color, 9);
    d.getLast()[4] = x;
    d.getLast()[5] = y;
    d.getLast()[6] = w;
    d.getLast()[7] = h;
    d.getLast()[8] = filled;
  }
}
