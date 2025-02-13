package entities;

import java.util.Collections;
import java.util.List;

import entities.generic.Entity;
import genetics.PlantGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

/**
 * A class that holds the properties of a plant entity.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Plant extends Entity {
  protected PlantGenetics genetics; // Re-cast to PlantGenetics.

  /**
   * Constructor -- Create a new plant entity with the given genetics and position.
   */
  public Plant(PlantGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
  }

  /**
   * Spawn new plants around this plant. The new plants have the same
   * genetics as the parent plant (may be mutated).
   * @return A list of new plants if the plant can multiply, empty list otherwise.
   */
  public List<Plant> multiply() {
    if (!canMultiply() || Math.random() > genetics.getMultiplyingRate()) return Collections.emptyList();
    int seeds = genetics.getNumberOfSeeds();

    Plant[] newPlants = new Plant[seeds];
    for (int i = 0; i < seeds; i++) {
      Vector seedPos = position.getRandomPointInRadius(genetics.getMaxOffspringSpawnDistance());
      newPlants[i] = new Plant(genetics.getOffspringGenetics(), seedPos);
      // TODO: Proper genetics system for plants (mutation) (generic system needed).
    }

    return List.of(newPlants);
  }

  /**
   * Update this plant entity.
   */
  @Override
  public void update(Field field, double deltaTime) {
    if (!isAlive()) return;
    super.update(field, deltaTime);
    
    List<Plant> newPlants = multiply();
    
    for (Plant plant : newPlants) {
      field.putInBounds(plant, plant.getSize());
      field.addEntity(plant);
    }

    handleOvercrowding(field.getEntities());
  }

  /**
  * Draw the plant entity to the display as a triangle.
  * @param display The display to draw to.
  * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
  */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (getCurrentSize() / scaleFactor);
    int x = (int) (position.x() / scaleFactor);
    int y = (int) (position.y() / scaleFactor);
    display.drawEqualTriangle(x, y, size, genetics.getColour());
  }
}