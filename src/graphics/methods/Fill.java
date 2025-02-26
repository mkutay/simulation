package graphics.methods;

/**
 * Method to "fill" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Fill extends Method {
  /**
   * Add a fill method call to the data.
   */
  public void add(int i, int[] colour) {
    super.add(i, colour, 4);
  }
}
