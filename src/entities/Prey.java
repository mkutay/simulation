package entities;

import entities.generic.*;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;
import simulation.simulationData.Data;
import util.Vector;

import java.util.List;

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
  public Prey(AnimalGenetics genetics, Vector location, Data data) {
    super(genetics, location, data);
  }

  /**
   * @return Predator if there is a predator nearby, null otherwise.
   */
  private Predator searchForPredators(List<Entity> entities) {
    Predator nearestEntity = null;
    double closestDistance = Double.MAX_VALUE;
    for (Entity entity : entities) {
      if (entity instanceof Predator predator && predator.canEat(this)) {
        // If this animal is food of the predator.
        double distance = entity.getPosition().subtract(position).getMagnitudeSquared();
        if (distance < closestDistance) {
          nearestEntity = predator;
          closestDistance = distance;
        }
      }
    }

    return nearestEntity;
  }

  /**
   * Runs away from the specified entity. Does nothing on null entity.
   * @param entity The entity to run away from.
   * @param deltaTime Delta time of simulation.
   */
  private void flee(Entity entity, double deltaTime) {
    if (entity == null) return;

    double speed = genetics.getMaxSpeed() * deltaTime;
    // double direction = position.subtract(entity.getPosition()).getAngle();
    // Vector movement = new Vector(Math.cos(direction) * speed, Math.sin(direction) * speed);
    Vector movement = position.subtract(entity.getPosition()).normalise().multiply(speed);
    position = position.add(movement);
  }

  @Override
  protected void updateBehaviour(Field field, List<Entity> nearbyEntities, double deltaTime) {
    boolean isHungry = foodLevel <= 0.5;
    boolean isDyingOfHunger = foodLevel <= 0.2; // Extreme case for prey to prioritise food over fleeing from predators.

    if (isHungry) eat(nearbyEntities);

    if (!isDyingOfHunger) { // If not dying of hunger, attempt to flee from predators.
      Predator nearestPredator = searchForPredators(nearbyEntities);
      if (nearestPredator != null) { // If a predator is found, flee!
        flee(nearestPredator, deltaTime); // Prioritise fleeing from predators
        return; // Stop other behaviours from occurring
      }
    }

    boolean movingToFood = false;
    if (isHungry && !isMovingToMate) { // If is hungry and not currently attempting to mate
      movingToFood = moveToNearestFood(nearbyEntities, deltaTime);
    }

    if (!movingToFood) { // If not moving to food and not hungry, look for mate
      isMovingToMate = moveToNearestMate(nearbyEntities, deltaTime);
      if (!isMovingToMate) wander(field, deltaTime); // If cant find mate, wander.
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
    return new Prey(genetics, position, this.data);
  }
}