package entities;

import java.util.List;

import entities.generic.Entity;
import genetics.PlantGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

public class Plant extends Entity {
  public Plant(PlantGenetics genetics, Vector position) {
    super(genetics, position);
  }

  public List<Plant> multiply() {
    return null;
  }

  /**
  * Draw the plant entity to the display as a triangle
  * @param display the display to draw to*
  * @param scaleFactor the field scale factor for the position and size (for scaling screen size and simulation size)
  */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (genetics.getSize() / scaleFactor);
    int x = (int) (position.x / scaleFactor);
    int y = (int) (position.y / scaleFactor);
    display.drawEqualTriangle(x, y, size, genetics.getColour());
  }

  @Override
  public void update(Field field, double deltaTime) {

  }
}
