package entities;

import java.awt.Color;
import java.util.List;

import entities.generic.Entity;
import genetics.PlantGenetics;
import graphics.Display;

public class Plant extends Entity {
  public Plant(PlantGenetics genetics, Vector position) {
    super(genetics, position);
  }

  public double getSight() { return 0; }

  public List<Plant> multiply() {
    return null;
  }

  /**
  * Draw the entity to a display
  * @param display the display to draw to
  */
  @Override
  public void draw(Display display) {
    display.drawCircle((int) position.x, (int) position.y, genetics.getSize(), Color.GREEN);
  }
}
