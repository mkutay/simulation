package entities.generic;

import simulation.simulationData.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Handles the hunger and food level logic for animals. Contains the food level
 * of the animal, which is a double value between 0 and 1, and if the animal has 
 * eaten before. This is used to control breeding and to deplete the food level 
 * more.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class AnimalHungerController {
  private final Animal animal; // The animal this controller is controlling.

  protected double foodLevel; // The current food level of the animal (as a ratio between 0 and 1).
  private boolean hasEaten = false; // Stores if the animal has eaten at least once or not -- used for breeding.

  /**
   * Constructor.
   * @param animal The animal to control the hunger for.
   */
  public AnimalHungerController(Animal animal) {
    this.animal = animal;
    foodLevel = 0.4; // Spawn with 40% food.
  }

  /**
   * Attempts to eat any colliding entities.
   * @param nearbyEntities The entities that will be searched through -- are in the sight radius of this animal.
   */
  public void eat(List<Entity> nearbyEntities) {
    for (Entity entity : nearbyEntities) {
      if (!this.canEat(entity) || !animal.isColliding(entity)) continue;

      double entitySizeRatio = (double) entity.getSize() / animal.getSize();
      double foodValue = (entity instanceof Animal ? Data.getFoodValueForAnimals() : Data.getFoodValueForPlants());
      double foodQuantity = entitySizeRatio * foodValue;
      foodLevel += foodQuantity;
      this.hasEaten = true; // Mark this animal as having eaten at least once -- to control breeding.
      entity.setDead();
    }

    foodLevel = Math.min(foodLevel, 1); // Clamp food from exceeding max food of animal, which is 1.
  }


  /**
   * Sets animal as dead if it has no food, decrements food level proportion to deltaTime.
   * @param numberOfOffsprings The number of offsprings the animal had in the current simulation step.
   * @param deltaTime Delta time.
   */
  public void handleHunger(double deltaTime, int numberOfOffsprings) {
    // Decrease food level based on current distance travelled, which is proportional to speed.
    double distanceTraveled = animal.movementController.getDistanceTravelled();
    double hungerDrainPerTick = Data.getAnimalHungerDrain() * distanceTraveled * deltaTime;

    // If sleeping, consume 50% less food, if hasn't eaten yet, consume 125% more food to increase fragility of children.
    foodLevel -= hungerDrainPerTick * (animal.isAsleep ? 0.5 : 1) * (hasEaten ? 1 : 2.25);
    foodLevel -= numberOfOffsprings / (numberOfOffsprings + 1 / Data.getAnimalBreedingCost()); // Food cost for breeding.
    if (foodLevel <= 0) animal.setDead();
  }

  /**
   * Checks if this animal can eat a specified entity. Also checks if the given 
   * entity is alive or not.
   * @param entity The entity to check if this animal can eat.
   * @return True if this animal can eat the entity, false otherwise.
   */
  public boolean canEat(Entity entity) {
    return Arrays.asList(animal.genetics.getEats()).contains(entity.getName()) && entity.isAlive();
  }

  /**
   * The animal is considered to be hungry if it has also not eaten at least once.
   * @return True if the animal is hungry, false otherwise.
   */
  public boolean isHungry() {
    return foodLevel <= Data.getAnimalHungerThreshold() || !this.hasEaten;
  }

  /**
   * @return True if the animal is dying of hunger, false otherwise.
   */
  public boolean isDyingOfHunger() {
    return foodLevel <= Data.getAnimalDyingOfHungerThreshold();
  }

  // Getters:
  public boolean hasEaten() { return hasEaten; }
  public double getFoodLevel() { return foodLevel; }
}
