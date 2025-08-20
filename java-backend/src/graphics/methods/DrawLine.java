package graphics.methods;

/**
 * Method to "draw a line" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawLine extends Method {
  /**
   * Add a draw line method call to the data.
   */
  public void add(int i, int x1, int y1, int x2, int y2, int[] color) {
    super.add(i, color, 8);
    d.add(x1);
    d.add(y1);
    d.add(x2);
    d.add(y2);
  }
}
