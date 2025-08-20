package entities.generic;

import simulation.Field;

import java.util.List;

/**
 * Handles animal behaviour. All animal behaviour, predator or prey, is identical.
 * They move to food and away from things that eat them. They try to move to
 * mates when they are not hungry. They sleep at night if they are not hungry.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class AnimalBehaviourController {
  private final Animal animal; // The animal this controller is controlling.

  private boolean isMovingToMate = false; // Stores if the animal is currently attempting to mate.

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

    // If not dying of hunger and not moving to mate, attempt to flee from predators.
    if (!isDyingOfHunger && !isMovingToMate) {
       // If fleeing, stop other behaviour:
      if (animal.movementController.fleeFromPredators(nearbyEntities, deltaTime)) return;
    }

    // If not hungry, its nighttime, isn't moving to mate and no predator is nearby, sleep (do nothing):
    if (!isHungry && !field.environment.isDay() && !isMovingToMate) {
      animal.isAsleep = true;
      return; // Stop other behaviour from occurring.
    }

    boolean movingToFood = false;
    // If is hungry and not currently attempting to mate, move to food:
    if (isHungry && !isMovingToMate) {
      movingToFood = animal.movementController.moveToNearestFood(nearbyEntities, deltaTime);
    }

    // If not moving to food and not hungry, look for mate:
    if (!movingToFood) {
      isMovingToMate = animal.movementController.moveToNearestMate(nearbyEntities, deltaTime);
      if (!isMovingToMate) { // If can't find mate, just wander.
        animal.movementController.wander(field, deltaTime);
      }
    }
  }
}