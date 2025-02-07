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
   * @return the number of plans in proximity to the current plant
   */
  private int countNearbyPlants(Field field) {
    List<Entity> entities = field.getEntities().stream()
            .filter(entity -> entity instanceof Plant)
            .toList(); // Creates an immutable filtered list

    return searchNearbyEntities(entities, genetics.getSize() * 3).size();
  }

  /**
   * Spawns new plants around it. The new plants have the same genetics as the parent plant (may be mutated).
   * TODO: The plants multiply too quickly and too much. Maybe a collision system is needed.
   * @return A list of new plants if the plant can multiply, null otherwise.
   */
  public List<Plant> multiply() {
    if (!canMultiply() || Math.random() > genetics.getMultiplyingRate()) return null;

    int seeds = genetics.getNumberOfSeeds();
    Plant[] newPlants = new Plant[seeds];
    for (int i = 0; i < seeds; i++) {
      double spawnAngleFromParent = Math.random() * Math.PI * 2;
      double spawnDistanceFromParent = (Math.random()) * genetics.getSize() * 6 + genetics.getSize(); //TODO maybe add spawn distance as a genetic parameter

      double seedX = this.position.x + Math.cos(spawnAngleFromParent) * spawnDistanceFromParent;
      double seedY = this.position.y + Math.sin(spawnAngleFromParent) * spawnDistanceFromParent;

      Vector seedPos = new Vector(seedX, seedY);
      newPlants[i] = new Plant(genetics, seedPos); //TODO proper genetics system for plants (mutation) (generic system needed)
    }
    return List.of(newPlants);
  }

  @Override
  public void update(Field field, double deltaTime) {
    super.update(field, deltaTime);

    if (countNearbyPlants(field) > 2){ //Overcrowding
      setDead();
    }

    List<Plant> newPlants = multiply();
    if (newPlants == null) return;

    for (Plant plant : newPlants) {
      field.putInBounds(plant, plant.getSize());
      field.safeAddEntity(plant);
    }
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