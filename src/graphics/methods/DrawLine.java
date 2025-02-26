package graphics.methods;

/**
 * Method to "draw a line" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawLine extends Method {
  public void add(int i, int x1, int y1, int x2, int y2, int[] color) {
    super.add(i, color, 8);
    d.getLast()[4] = x1;
    d.getLast()[5] = y1;
    d.getLast()[6] = x2;
    d.getLast()[7] = y2;
  }
}
