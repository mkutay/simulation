package entities.generic;

import entities.Predator;
import simulation.Field;

import java.util.List;
import java.util.function.Predicate;

/**
 * Handles animal behaviour. All animal behaviour, predator or prey, is identical.
 * They move to food and away from things that eat them.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class AnimalBehaviourController {
  private final Animal animal; // The animal this controller is controlling

  /**
   * Constructor.
   * @param animal The animal to control the behaviour for.
   */
  public AnimalBehaviourController(Animal animal) {
    this.animal = animal;
  }

  /**
   * Updates the behaviour of the animal, specifically for movement.
   * @param field The field the animal is in.
   * @param nearbyEntities The entities in the sight radius of the animal.
   * @param deltaTime The delta time.
   */
  public void updateBehaviour(Field field, List<Entity> nearbyEntities, double deltaTime) {
    animal.isAsleep = false;

    boolean isHungry = animal.hungerController.isHungry();
    // Extreme case for prey to prioritise food over fleeing from predators:
    boolean isDyingOfHunger = animal.hungerController.isDyingOfHunger();

    if (isHungry) animal.hungerController.eat(nearbyEntities);

    if (!isDyingOfHunger && !animal.isMovingToMate) { // If not dying of hunger, attempt to flee from predators.
       // If fleeing, stop other behaviour:
      if (fleeFromPredators(nearbyEntities, deltaTime)) return;
    }

    // If not hungry, its nighttime, isn't truing to mate and no predator is nearby, sleep (do nothing):
    if (!isHungry && !field.environment.isDay() && !animal.isMovingToMate) {
      animal.isAsleep = true;
      return; // Stop other behaviour from occurring
    }

    boolean movingToFood = false;
    if (isHungry && !animal.isMovingToMate) { // If is hungry and not currently attempting to mate
      movingToFood = animal.movementController.moveToNearestFood(nearbyEntities, deltaTime);
    }

    if (!movingToFood) { // If not moving to food and not hungry, look for mate
      animal.isMovingToMate = animal.movementController.moveToNearestMate(nearbyEntities, deltaTime);
      if (!animal.isMovingToMate) { // If can't find mate, wander.
        animal.movementController.wander(field, deltaTime);
      }
    }
  }

  /**
   * Searches for nearby predators and flees if found.
   * @return True if it flees, false otherwise.
   */
  private boolean fleeFromPredators(List<Entity> nearbyEntities, double deltaTime) {
    // Find the nearest predator:
    Predicate<Entity> condition = e -> e instanceof Predator p && p.getHungerController().canEat(animal);
    Predator nearestPredator = (Predator) animal.movementController.getNearestEntity(nearbyEntities, condition);

    if (nearestPredator == null) return false;

    // If a predator is found, flee!
    animal.movementController.fleeFromEntity(nearestPredator, deltaTime);
    return true;
  }
}