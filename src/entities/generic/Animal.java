package entities.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import util.Vector;
import genetics.AnimalGenetics;
import simulation.Field;

/**
 * Abstract class for all animals in the simulation.
 * Contains methods for moving, eating, breeding, and handling hunger.
 * 
 * @author Mehmet Kutay Bozkurt and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; // Re-cast to AnimalGenetics
  protected double foodLevel; // The current food level of the animal
  private double direction; // The direction the animal is moving in

  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
    this.foodLevel = genetics.getMaxFoodLevel() / 2; // Start at 50% hunger.
    this.direction = Math.random() * Math.PI * 2;
  }

  /**
   * Move the entity around randomly, bouncing off of the edges.
   * TODO: Revamp.
   */
  public void wander(Field field, double deltaTime) {
    direction += (Math.random() - 0.5) * Math.PI * 0.1;
    if (field.isOutOfBounds(position, getSize())) {
      Vector centerOffset = field.getSize().multiply(0.5).subtract(position);
      direction = centerOffset.getAngle() + (Math.random() - 0.5) * Math.PI;
    }
    
    double speed = getMaxSpeed() * 0.6 * deltaTime; // 60% move speed when wandering
    Vector movement = new Vector(Math.cos(direction) * speed, Math.sin(direction) * speed);
    position = position.add(movement);
  }

  /**
   * Moves in the direction of another entity.
   * @param entity The entity to move to.
   * @param deltaTime Delta time.
   */
  public void moveToEntity(Entity entity, double deltaTime) {
    double speed = getMaxSpeed() * deltaTime;
    double direction = entity.position.subtract(position).getAngle();
    Vector movement = new Vector(Math.cos(direction) * speed, Math.sin(direction) * speed);
    position = position.add(movement);
  }

  /**
   * Search a list of nearby entities and move to the nearest food entity.
   * @param entities The list of nearby entities to search.
   * @param deltaTime The deltatime of the simulation.
   * @return True if succesfully found a food entity to move to, false otherwise.
   */
  public boolean moveToNearestFood(List<Entity> entities, double deltaTime) {
    Entity nearestEntity = null;
    double closestDistance = Double.MAX_VALUE;
    for (Entity entity : entities) {
      if (canEat(entity) && entity.isAlive()) {
        double distance = entity.position.subtract(position).getMagnitudeSquared();
        if (distance < closestDistance) {
          nearestEntity = entity;
          closestDistance = distance;
        }
      }
    }

    if (nearestEntity == null) return false;
    foodLevel -= getMaxSpeed() * 0.03 * deltaTime; // Decrement the food level more when trying to catch food.
    moveToEntity(nearestEntity, deltaTime);
    return true;
  }

  /**
   * Checks if this animal can eat a specified entity
   * @param entity The entity to check if this animal can eat.
   * @return True if this animal can eat the entity, false otherwise.
   */
  public boolean canEat(Entity entity) {
    return Arrays.asList(getEats()).contains(entity.getName());
  }

  /**
   * Attempts to eat any colliding entities.
   * @param nearbyEntities The entities in the sight radius of this animal.
   */
  public void eat(List<Entity> nearbyEntities) {
    if (nearbyEntities == null) return;

    for (Entity entity : nearbyEntities) {
      if (entity.isAlive() && canEat(entity) && isColliding(entity)) {
        foodLevel += entity.getSize() * 1.2;
        entity.setDead();
      }
    }

    foodLevel = Math.min(foodLevel, genetics.getMaxFoodLevel()); // Clamp food from exceeding max food of animal.
  }

  /**
   * Sets animal as dead if no food, decrements food level proportion to deltaTime.
   * TODO: Food level decrements based on speed.
   * TODO: Food level decrements when reproducing.
   */
  public void handleHunger(double deltaTime) {
    foodLevel -= genetics.getMaxFoodLevel() * 0.02 * deltaTime;
    if (foodLevel <= 0) {
      setDead();
    }
  }

  /**
   * @return Whether the animal can multiply or not, and also according to the food level.
   */
  @Override
  protected boolean canMultiply() {
    return super.canMultiply() && foodLevel >= genetics.getMaxFoodLevel() * 0.2;
  }

  /**
   * Update the animal.
   */
  @Override
  public void update(Field field, double deltaTime) {
    if (!isAlive()) return;

    List<Animal> newEntities = breed(field);

    for (Animal entity : newEntities) {
      field.putInBounds(entity, entity.getSize());
      field.addEntity(entity);
    }

    foodLevel -= newEntities.size() * genetics.getMaxFoodLevel() * 0.1 * deltaTime;

    List<Entity> nearbyEntities = searchNearbyEntities(field.getEntities(), genetics.getSight());

    eat(nearbyEntities); //TODO eat only when hungry
    handleHunger(deltaTime);

    boolean movingToFood = moveToNearestFood(nearbyEntities, deltaTime);
    if (!movingToFood) wander(field, deltaTime);

    super.update(field, deltaTime);
  }

  /**
   * Used to generate an offspring of the animal after breeding
   * @return a new Animal with the specified genetics and spawn position
   */
  protected abstract Animal createOffspring(AnimalGenetics genetics, Vector position);

  /**
   * Breeds with a random mate (opposite gender) from the list of entities in sight.
   * @return A list of offspring from the breeding, empty if no breeding occurred.
   */
  protected List<Animal> breed(Field field) {
    if (!isAlive() || !canMultiply() || Math.random() > genetics.getMultiplyingRate()) return Collections.emptyList();

    Animal mateEntity = getRandomMate(field.getEntities());
    if (mateEntity == null) return Collections.emptyList(); //If no valid mate entity, end

    int litterSize = (int) (Math.random() * Math.min(genetics.getMaxLitterSize(), mateEntity.genetics.getMaxLitterSize())) + 1;
    List<Animal> offspring = new ArrayList<>();
    for (int i = 0; i < litterSize; i++) {
      AnimalGenetics childGenetics = genetics.breed(mateEntity.genetics);
      Vector newPos = position.getRandomPointInRadius(genetics.getMaxOffspringSpawnDistance());
      offspring.add(createOffspring(childGenetics, newPos));
    }
    return offspring;
  }

  /**
   * Checks if they have different genders and if the potential mate is alive and can multiply.
   * @param others The list of entities to search for a mate.
   * @return A random mate from the list of entities, null if no mate found.
   */
  public Animal getRandomMate(List<Entity> others) {
    //TODO this isnt amazing for performance, later try avoid using searchNearbyEntities more than necessary as it greatly reduces performance
    List<Entity> entities = getSameSpecies(searchNearbyEntities(others, genetics.getSight() * 0.5));
    List<Animal> potentialMates = entities.stream()
      .filter(entity -> entity instanceof Animal) //entity is animal
      .filter(entity -> entity.getName().equals(getName())) //entity is same species as this animal
      .map(entity -> (Animal) entity)
      .filter(animal -> animal.genetics.getGender() != genetics.getGender())
      .filter(animal -> animal.canMultiply() && animal.isAlive())
      .toList();

    if (potentialMates.isEmpty()) return null;
    return potentialMates.get((int) (Math.random() * potentialMates.size()));
  }

  // Getters:
  public String[] getEats() { return genetics.getEats(); }
  public double getMaxSpeed() { return genetics.getMaxSpeed(); }
  public double getFoodLevel() { return foodLevel; }
}