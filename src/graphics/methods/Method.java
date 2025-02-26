package graphics.methods;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for all the methods that can be used to draw on the screen,
 * such as circles, rectangles, lines, etc.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public abstract class Method {
  public List<Object[]> d = new ArrayList<>(); // Data of the method.

  /**
   * Add a new entity to the data.
   * @param id The index of the entity.
   * @param colour The colour of the entity.
   * @param length The length of the data object.
   */
  protected void add(int id, int[] colour, int length) {
    d.add(new Object[length]);
    d.getLast()[0] = id;
    d.getLast()[1] = colour[0];
    d.getLast()[2] = colour[1];
    d.getLast()[3] = colour[2];
  }

  /**
   * Reverse the data.
   */
  public void reverse() {
    d = d.reversed();
  }
}
