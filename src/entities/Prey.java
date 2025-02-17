package entities;

import entities.generic.*;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

import java.util.List;
import java.util.function.Predicate;

/**
 * An arbitrary prey entity that moves around randomly and can reproduce.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Prey extends Animal {
  /**
   * Constructor -- Create a new prey entity with the given genetics and location.
   */
  public Prey(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Updates the behaviour of the animal, specifically for movement.
   * @param field The field the animal is in.
   * @param nearbyEntities The entities in the sight radius of the animal.
   * @param deltaTime The delta time.
   */
  @Override
  protected void updateBehaviour(Field field, List<Entity> nearbyEntities, double deltaTime) {
    boolean isHungry = hungerController.isHungry();
    boolean isDyingOfHunger = hungerController.isDyingOfHunger(); // Extreme case for prey to prioritise food over fleeing from predators.
    isAsleep = false;

    if (isHungry) hungerController.eat(nearbyEntities);

    if (!isDyingOfHunger) { // If not dying of hunger, attempt to flee from predators.
      // Find the nearest predator:
      Predicate<Entity> condition = entity -> entity instanceof Predator predator && predator.getHungerController().canEat(this);
      Predator nearestPredator = (Predator) movementController.getNearestEntity(nearbyEntities, condition);
      
      if (nearestPredator != null) { // If a predator is found, flee!
        movementController.fleeFromEntity(nearestPredator, deltaTime); // Prioritise fleeing from predators
        return; // Stop other behaviours from occurring
      }
    }

    if (!isHungry && !field.isDay()) { isAsleep = true; return; } //If not hungry and its nighttime and no predator is nearby, sleep (do nothing)


    boolean movingToFood = false;
    if (isHungry && !this.isMovingToMate) { // If is hungry and not currently attempting to mate
      movingToFood = movementController.moveToNearestFood(nearbyEntities, deltaTime);
    }

    if (!movingToFood) { // If not moving to food and not hungry, look for mate
      this.isMovingToMate = movementController.moveToNearestMate(nearbyEntities, deltaTime);
      if (!this.isMovingToMate) movementController.wander(field, deltaTime); // If cant find mate, wander.
    }
  }

  /**
   * Draw the prey entity to the display as a circle.
   * @param display The display to draw to.
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
   */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (getCurrentVisualSize() / scaleFactor);
    size = Math.max(1, size);
    int x = (int) (position.x() / scaleFactor);
    int y = (int) (position.y() / scaleFactor);
    display.drawCircle(x, y, size, genetics.getColour());
  }

  /**
   * Create a new prey entity with the given genetics and location.
   * Used for reproduction in the Animal class. Used to avoid code 
   * duplication.
   */
  @Override
  protected Animal createOffspring(AnimalGenetics genetics, Vector position) {
    return new Prey(genetics, position);
  }
}