package graphics.methods;

/**
 * Method to "draw text" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawText extends Method {
  /**
   * Add a draw text method call to the data.
   */
  public void add(int i, String text, int size, int x, int y, int[] color) {
    super.add(i, color, 8);
    d.getLast()[4] = text;
    d.getLast()[5] = size;
    d.getLast()[6] = x;
    d.getLast()[7] = y;
  }
}
