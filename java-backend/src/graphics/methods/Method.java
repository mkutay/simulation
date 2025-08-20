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
  public List<Object> d = new ArrayList<>(); // Data of the method.
  private transient int length;

  /**
   * Add a new entity to the data.
   * @param id The index of the entity.
   * @param colour The colour of the entity.
   */
  protected void add(int id, int[] colour, int length) {
    this.length = length;
    d.add(id);
    d.add(colour[0]);
    d.add(colour[1]);
    d.add(colour[2]);
  }

  /**
   * Reverse the data.
   */
  public void reverse() {
    d = d.reversed();
    for (int i = 0; i < d.size(); i += length) {
      int j = -(i % length) + length - 1;
      swapIndicies(i, j);
    }
  }

  private void swapIndicies(int i, int j) {
    Object tmp = d.get(i);
    d.set(i, d.get(j));
    d.set(j, tmp);
  }
}
