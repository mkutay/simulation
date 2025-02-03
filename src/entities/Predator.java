package entities;
import entities.generic.Animal;
import genetics.AnimalGenetics;
import graphics.Display;

import java.awt.*;

public class Predator extends Animal {
  public Predator(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Draw the entity to a display
   * @param display the display to draw to
   */
  @Override
  public void draw(Display display) {
    display.drawCircle((int) position.x, (int) position.y, genetics.getSize(), Color.RED);
  }
}
