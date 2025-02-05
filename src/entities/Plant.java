package entities;

import java.util.List;

import entities.generic.Entity;
import genetics.PlantGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

public class Plant extends Entity {
  protected PlantGenetics genetics;

  public Plant(PlantGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
  }

  /**
   * Miltiples. The new plants have the same genetics as the parent plant.
   * TODO: The plants multiply too quickly and too much. Maybe a collision system is needed.
   * @return A list of new plants if the plant can multiply, null otherwise
   */
  public List<Plant> multiply() {
    if (!canMultiply() || Math.random() > genetics.getMultiplyingRate()) return null;

    int seeds = genetics.getNumberOfSeeds();
    Plant[] newPlants = new Plant[seeds];
    for (int i = 0; i < seeds; i++) {
      Vector newPos = new Vector(this.position.x + 6 * (Math.random() * genetics.getSize() - genetics.getSize() / 2),
        this.position.y + 6 * (Math.random() * genetics.getSize() - genetics.getSize() / 2));
      newPlants[i] = new Plant(genetics, newPos);
    }
    return List.of(newPlants);
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
    super.update(field, deltaTime);
    List<Plant> newPlants = multiply();
    if (newPlants == null) return;
    for (Plant plant : newPlants) {
      field.putInBounds(plant, plant.getSize());
      field.addEntity(plant);
    }
  }
}
