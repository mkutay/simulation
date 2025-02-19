package entities.generic;

import simulation.Field;
import util.Utility;
import util.Vector;

import java.util.List;
import java.util.function.Predicate;

import entities.Predator;

/**
 * Handles all movement for an animal. This includes wandering, moving to food, moving
 * to mates, and fleeing from entities that can eat the animal.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class AnimalMovementController {
  private final Animal animal; // The animal this controller controls.

  private double direction; // The direction the animal is moving in (in radians).
  private Vector lastPosition; // The last position of the animal -- used to calculate speed.

  /**
   * Constructor.
   * @param animal The animal to control movement for.
   * @param position The initial position of the animal.
   */
  public AnimalMovementController(Animal animal, Vector position) {
    this.animal = animal;
    this.direction = Math.random() * Math.PI * 2;
    this.lastPosition = position;
  }

  /**
   * Move the entity around randomly, bouncing off of the edges.
   * @param field The field to move the entity in.
   * @param deltaTime The delta time.
   */
  public void wander(Field field, double deltaTime) {
    Vector currentPos = animal.getPosition();

    // Randomly change direction:
    direction += (Math.random() - 0.5) * Math.PI * 0.1;
    if (field.isOutOfBounds(currentPos, animal.getSize())) {
      Vector centerOffset = field.getSize().multiply(0.5).subtract(currentPos);
      direction = centerOffset.getAngle() + (Math.random() - 0.5) * Math.PI;
    }

    double speed = animal.genetics.getMaxSpeed() * 0.6 * deltaTime; // 60% move speed when wandering
    Vector movement = Vector.getVectorFromAngle(direction).multiply(speed);
    Vector newPosition = currentPos.add(movement);
    animal.setPosition(newPosition);
  }

  /**
   * Moves to the nearest entity that this animal can eat, returns false if there are none nearby.
   * @param entities The list of entities to search for food from.
   * @param deltaTime The delta time.
   * @return True if the entity is moving successfully, false if it is not moving.
   */
  public boolean moveToNearestFood(List<Entity> entities, double deltaTime) {
    Predicate<Entity> condition = animal.hungerController::canEat;
    Entity nearestEntity = getNearestEntity(entities, condition);
    if (nearestEntity == null) return false;

    moveToEntity(nearestEntity, deltaTime);
    return true;
  }

  /**
   * Moves to the nearest entity that this animal can breed with, returns false if there are none nearby.
   * @param entities The list of entities to search for mates from.
   * @param deltaTime The delta time.
   * @return True if the entity is moving successfully, false if it is not moving.
   */
  public boolean moveToNearestMate(List<Entity> entities, double deltaTime) {
    Predicate<Entity> condition = animal.breedingController::canMateWith;
    Entity nearestEntity = getNearestEntity(entities, condition);
    if (nearestEntity == null) return false;

    moveToEntity(nearestEntity, deltaTime);
    return true;
  }

  /**
   * Searches for nearby predators and flees if found. Predator to this animal 
   * is another animal that can eat it.
   * @param nearbyEntities The list of nearby entities to look through.
   * @param deltaTime The delta time.
   * @return True if it flees, false otherwise.
   */
  public boolean fleeFromPredators(List<Entity> nearbyEntities, double deltaTime) {
    // Find the nearest predator using a predicate:
    Predicate<Entity> condition = e -> e instanceof Predator p && p.canEat(animal);
    Predator nearestPredator = (Predator) getNearestEntity(nearbyEntities, condition);
    if (nearestPredator == null) return false;

    // If a predator is found, flee!
    fleeFromEntity(nearestPredator, deltaTime);
    return true;
  }

  /**
   * Search a list of nearby entities and return the nearest entity satisfying the condition, or null.
   * @implNote If there are multiple entities at the same distance, the first one in the list is returned.
   * @param entities The list of nearby entities to search.
   * @param condition The condition to determine what entities to move towards.
   * @return The nearest entity satisfying the condition, null if none found.
   */
  private Entity getNearestEntity(List<Entity> entities, Predicate<Entity> condition) {
    Entity nearestEntity = null;
    double closestDistance = Double.MAX_VALUE;

    for (Entity entity : entities) {
      if (condition.test(entity)) {
        double distance = entity.position.subtract(animal.getPosition()).getMagnitudeSquared();
        if (distance < closestDistance) {
          nearestEntity = entity;
          closestDistance = distance;
        }
      }
    }

    return nearestEntity;
  }

  /**
   * Moves to or away from another entity.
   * @param entity The entity to move to.
   * @param deltaTime Delta time.
   * @param moveAwayFromEntity If true, run away from entity instead of towards entity.
   */
  private void moveRelativeToEntity(Entity entity, double deltaTime, boolean moveAwayFromEntity) {
    double speed = animal.genetics.getMaxSpeed() * deltaTime;
    Vector currentPos = animal.getPosition();

    Vector difference;
    if (moveAwayFromEntity) {
      difference = currentPos.subtract(entity.position);
    } else {
      difference = entity.position.subtract(currentPos);
    }

    // Do nothing if the entity is at the same position (avoids division by zero):
    if (difference.getMagnitudeSquared() < Utility.EPSILON) return;

    Vector movement = difference.normalise().multiply(speed);
    Vector newPosition = currentPos.add(movement);
    animal.setPosition(newPosition);
  }

  /**
   * Move to an entity.
   * @param entity The entity to move to.
   * @param deltaTime Delta time.
   */
  private void moveToEntity(Entity entity, double deltaTime) {
    moveRelativeToEntity(entity, deltaTime, false);
  }

  /**
   * Flee from an entity. Move in the other direction from the entity.
   * @param entity The entity to flee from.
   * @param deltaTime Delta time.
   */
  private void fleeFromEntity(Entity entity, double deltaTime) {
    moveRelativeToEntity(entity, deltaTime, true);
  }

  /**
   * Set the last position of the animal. Used for calculating speed.
   * @param lastPosition The last position of the animal.
   */
  public void setLastPosition(Vector lastPosition) {
    this.lastPosition = lastPosition;
  }

  /**
   * @implNote Uses current position and the last position to calculate.
   * @return The distance travelled in the current frame relative to the last.
   */
  public double getDistanceTravelled() {
    return animal.getPosition().subtract(lastPosition).getMagnitude();
  }
}
