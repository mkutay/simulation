package graphics.methods;

/**
 * Abstract class for all the methods that can be used to draw on the screen,
 * such as circles, rectangles, lines, etc.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public abstract class Method {
  public int i; // Index of the method call.
  public int[] c; // Colour of the method call.

  /**
   * Constructor.
   */
  public Method(int id, int[] colour) {
    this.i = id;
    this.c = colour;
  }
}
