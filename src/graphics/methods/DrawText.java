package graphics.methods;

/**
 * Method to "draw text" on the screen. Holds the data for the method call.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DrawText extends Method {
  public int x; // X-coordinate of the text.
  public int y; // Y-coordinate of the text.
  public String t; // The text itself/
  public int s; // The font size of the text.

  /**
   * Constructor.
   */
  public DrawText(int i, String text, int size, int x, int y, int[] color) {
    super(i, color);
    this.x = x;
    this.y = y;
    this.t = text;
    this.s = size;
  }
}
