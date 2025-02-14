package entities;

import entities.generic.*;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;
import util.Utility;
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
   * Runs away from the specified entity. Does nothing on null entity.
   * @param entity The entity to run away from.
   * @param deltaTime Delta time of simulation.
   */
  private void flee(Entity entity, double deltaTime) {
    if (entity == null) return;
    double speed = genetics.getMaxSpeed() * deltaTime;
    Vector difference = position.subtract(entity.getPosition());
    if (difference.getMagnitudeSquared() < Utility.EPSILON) return; // Do nothing if the entity is at the same position

    Vector movement = difference.normalise().multiply(speed);
    position = position.add(movement);
  }

  /**
   * Updates the behaviour of the animal, specifically for movement.
   * @param field The field the animal is in.
   * @param nearbyEntities The entities in the sight radius of the animal.
   * @param deltaTime The delta time.
   */
  @Override
  protected void updateBehaviour(Field field, List<Entity> nearbyEntities, double deltaTime) {
    boolean isHungry = foodLevel <= 0.5;
    boolean isDyingOfHunger = foodLevel <= 0.2; // Extreme case for prey to prioritise food over fleeing from predators.

    if (isHungry) eat(nearbyEntities);

    if (!isDyingOfHunger) { // If not dying of hunger, attempt to flee from predators.
      // Find the nearest predator:
      Predicate<Entity> condition = entity -> entity instanceof Predator predator && predator.canEat(this);
      Predator nearestPredator = (Predator) this.getNearestEntity(nearbyEntities, condition);
      
      if (nearestPredator != null) { // If a predator is found, flee!
        flee(nearestPredator, deltaTime); // Prioritise fleeing from predators
        return; // Stop other behaviours from occurring
      }
    }

    boolean movingToFood = false;
    if (isHungry && !this.isMovingToMate) { // If is hungry and not currently attempting to mate
      movingToFood = moveToNearestFood(nearbyEntities, deltaTime);
    }

    if (!movingToFood) { // If not moving to food and not hungry, look for mate
      this.isMovingToMate = moveToNearestMate(nearbyEntities, deltaTime);
      if (!this.isMovingToMate) wander(field, deltaTime); // If cant find mate, wander.
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
    int x = (int) (position.x() / scaleFactor);
    int y = (int) (position.y() / scaleFactor);
    display.drawCircle(x, y, size, genetics.getColour());
  }

  /**
   * Create a new prey entity with the given genetics and location.
   * Used for reproduction in the Animal class.
   */
  @Override
  protected Animal createOffspring(AnimalGenetics genetics, Vector position) {
    return new Prey(genetics, position);
  }
}