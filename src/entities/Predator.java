package entities;

import entities.generic.*;
import genetics.AnimalGenetics;
import graphics.Display;
import util.Vector;

public class Predator extends Animal {
  public Predator(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Draw the predator entity to the display as a square.
   * @param display The display to draw to.
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
   */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (genetics.getSize() / scaleFactor);
    int x = (int) ((position.x - (double) size / 2) / scaleFactor);  // Draw rectangle centered around x, y of predator.
    int y = (int) ((position.y - (double) size / 2) / scaleFactor);

    display.drawRectangle(x, y, size * 2, size * 2, genetics.getColour());
  }

  @Override
  protected Animal createOffspring(AnimalGenetics genetics, Vector position) {
    return new Predator(genetics, position);
  }
}
