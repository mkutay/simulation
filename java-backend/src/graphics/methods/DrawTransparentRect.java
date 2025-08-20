package graphics.methods;

/**
 * Method to "draw a transparent rectangle" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawTransparentRect extends Method {
  /**
   * Add a draw transparent rectangle method call to the data.
   */
  public void add(int i, int x, int y, int w, int h, int[] color, double a) {
    super.add(i, color, 9);
    d.add(x);
    d.add(y);
    d.add(w);
    d.add(h);
    d.add(a);
  }
}
