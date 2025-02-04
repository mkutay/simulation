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

  public List<Plant> multiply() {
    return null;
  }

  /**
  * Draw the plant entity to the display as a triangle
  * @param display the display to draw to
  */
  @Override
  public void draw(Display display) {
    //display.drawCircle((int) position.x, (int) position.y, genetics.getSize(), genetics.getColour());
    display.drawEqualTriangle((int) position.x, (int) position.y, genetics.getSize(), genetics.getColour());
  }
}
