package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.generic.Entity;
import genetics.PlantGenetics;
import graphics.Display;
import simulation.Field;
import simulation.environment.Weather;
import util.Vector;

/**
 * A class that holds the properties of a plant entity. Plants can multiply and
 * spread their seeds (basically new plants).
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
   * Spawn new plants around this plant. The new plants have the same genetics
   * as the parent plant, though it may be mutated.
   * @return A list of new plants if the plant can multiply, empty list otherwise.
   */
  public List<Plant> multiply(Field field) {
    // When raining, multiplication rate increases by a set factor in the genetics:
    double growthFactor = field.environment.getWeather() == Weather.RAINING ? genetics.getRainingGrowthFactor() : 1;
    double multiplyingRate = Math.min(genetics.getMultiplyingRate() * growthFactor, 1);

    if (!(canMultiply() && Math.random() < multiplyingRate)) return Collections.emptyList();

    if (!field.environment.isDay()) { // If it's night, very low odds of multiplying.
      if (Math.random() > 0.3) {
        return Collections.emptyList();
      }
    }

    // Growth factor affects number of seeds and range of growth:
    int seeds = (int) (genetics.getNumberOfSeeds() * growthFactor);
    List<Plant> newPlants = new ArrayList<>();
    for (int i = 0; i < seeds; i++) {
      Vector seedPos = position.getRandomPointInRadius(genetics.getMaxOffspringSpawnDistance() + growthFactor - 1);
      newPlants.add(new Plant(genetics.getOffspringGenetics(), seedPos));
    }

    return newPlants;
  }

  /**
   * Update this plant entity. This includes multiplying and handling overcrowding.
   */
  @Override
  public void update(Field field, double deltaTime) {
    if (!isAlive()) return;
    super.update(field, deltaTime);

    List<Entity> nearbyEntities = searchNearbyEntities(field, genetics.getOvercrowdingRadius());

    List<Plant> newPlants = multiply(field);
    for (Plant plant : newPlants) {
      field.putInBounds(plant, plant.getSize());
      field.addEntity(plant);
    }

    handleOvercrowding(nearbyEntities);
  }

  /**
  * Draw the plant entity to the display as a triangle.
  * @param display The display to draw to.
  * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
  */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (getCurrentVisualSize() / scaleFactor);
    size = Math.max(2, size);
    int x = (int) (position.x() / scaleFactor);
    int y = (int) (position.y() / scaleFactor);
    display.drawEqualTriangle(x, y, size, genetics.getColour());
  }
}