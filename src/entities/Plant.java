package entities;

import java.util.Collections;
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
   * Spawns new plants around it. The new plants have the same genetics as the parent plant (may be mutated).
   * @return A list of new plants if the plant can multiply, empty list otherwise.
   */
  public List<Plant> multiply() {
    if (!canMultiply() || Math.random() > genetics.getMultiplyingRate()) return Collections.emptyList();
    int seeds = genetics.getNumberOfSeeds();

    Plant[] newPlants = new Plant[seeds];
    for (int i = 0; i < seeds; i++) {
      Vector seedPos = position.getRandomPointInRadius(genetics.getMaxOffspringSpawnDistance());
      newPlants[i] = new Plant(genetics, seedPos);
      // TODO: Proper genetics system for plants (mutation) (generic system needed).
    }

    return List.of(newPlants);
  }

  @Override
  public void update(Field field, double deltaTime) {
    if (!isAlive()) return;
    
    List<Plant> newPlants = multiply();
    
    for (Plant plant : newPlants) {
      field.putInBounds(plant, plant.getSize());
      field.addEntity(plant);
    }
    
    super.update(field, deltaTime);
  }

  /**
  * Draw the plant entity to the display as a triangle.
  * @param display The display to draw to.
  * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
  */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (genetics.getSize() / scaleFactor);
    int x = (int) (position.x / scaleFactor);
    int y = (int) (position.y / scaleFactor);
    display.drawEqualTriangle(x, y, size, genetics.getColour());
  }
}