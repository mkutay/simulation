package entities;

import entities.generic.*;
import genetics.AnimalGenetics;
import graphics.Display;
import util.Vector;

/**
 * A class for representing an arbitrary prey entity.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Prey extends Animal {
  /**
   * Constructor -- Create a new prey entity with the given genetics and location.
   */
  public Prey(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Draw the prey entity to the display as a circle.
   * @param display The display to draw to.
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
   */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (getCurrentVisualSize() / scaleFactor);
    size = Math.max(1, size);
    int x = (int) (position.x() / scaleFactor);
    int y = (int) (position.y() / scaleFactor);
    display.drawCircle(x, y, size, genetics.getColour());
  }

  /**
   * Create a new prey entity with the given genetics and location.
   * Used for reproduction in the Animal class. Used to avoid code 
   * duplication.
   */
  @Override
  protected Animal createOffspring(AnimalGenetics genetics, Vector position) {
    return new Prey(genetics, position);
  }
}