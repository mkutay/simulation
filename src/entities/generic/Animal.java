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
  private Vector lastPosition; // The last position of the animal -- used to calculate speed.
  private boolean hasEaten = false; // Stores if the animal has eaten at least once or not.

  private final static double GLOBAL_FOOD_VALUE = 0.1; // Controls the value of food
  private final static double HUNGER_DRAIN = 0.005; // Controls rate of foodLevel depletion over time
  private final static double BREEDING_HUNGER_COST = 0.5; // Scales how much food is consumed on breeding. 0 for no food cost

  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
    this.foodLevel = 0.5; //Spawn with 50% food
    this.direction = Math.random() * Math.PI * 2;
    this.lastPosition = position;
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
        double entitySizeRatio = (double) entity.getSize() / getSize();
        foodLevel += entitySizeRatio * GLOBAL_FOOD_VALUE;
        hasEaten = true; //Mark this animal as having eaten at least once
        entity.setDead();
      }
    }

    foodLevel = Math.min(foodLevel, 1); // Clamp food from exceeding max food of animal (1).
  }

  /**
   * Sets animal as dead if no food, decrements food level proportion to deltaTime.
   * @param deltaTime Delta time.
   * @param numberOfOffsprings The number of offsprings the animal had in the current simulation step
   */
  public void handleHunger(double deltaTime, int numberOfOffsprings) {
    // Decrease food level based on current speed
    double distanceTraveled = position.subtract(lastPosition).getMagnitude();
    double currentSpeed = distanceTraveled / deltaTime;
    double hungerDrainPerTick = HUNGER_DRAIN * currentSpeed * deltaTime; // * genetics.getSight(); //TODO sight affects hunger drain as balancing system

    foodLevel -= hungerDrainPerTick;
    foodLevel -= numberOfOffsprings/(numberOfOffsprings + 1/BREEDING_HUNGER_COST);

    if (foodLevel <= 0) {
      setDead();
    }
  }

  /**
   * Update the animal.
   */
  @Override
  public void update(Field field, double deltaTime) {
    if (!isAlive()) return;
    super.update(field, deltaTime);

    List<Entity> nearbyEntities = searchNearbyEntities(field.getEntities(), genetics.getSight());

    List<Animal> newEntities = breed(nearbyEntities);
    for (Animal entity : newEntities) {
      field.putInBounds(entity, entity.getSize());
      field.addEntity(entity);
    }

    boolean isHungry = foodLevel <= 0.5;

    if (isHungry){eat(nearbyEntities);}

    handleHunger(deltaTime, newEntities.size());


    this.lastPosition = this.position;

    if (isHungry) {
      boolean movingToFood = moveToNearestFood(nearbyEntities, deltaTime);
      if (!movingToFood) wander(field, deltaTime);
    } else {
      wander(field, deltaTime);
    }

    handleOvercrowding(nearbyEntities);
  }

  /**
   * Used to generate an offspring of the animal after breeding
   * @return a new Animal with the specified genetics and spawn position
   */
  protected abstract Animal createOffspring(AnimalGenetics genetics, Vector position);

  /**
   * Animals can only breed if they have eaten food once in their life
   * @return true if this animal can breed/multiply, false otherwise
   */
  @Override
  protected boolean canMultiply() {
    return super.canMultiply() && hasEaten;
  }

  /**
   * Breeds with a random mate (opposite gender) from the list of entities in sight.
   * @param nearbyEntities The list of entities in sight of this animal.
   * @return A list of offspring from the breeding, empty if no breeding occurred.
   */
  protected List<Animal> breed(List<Entity> nearbyEntities) {
    if (!canMultiply() || Math.random() > genetics.getMultiplyingRate()) return Collections.emptyList();

    Animal mateEntity = getRandomMate(nearbyEntities);
    if (mateEntity == null) return Collections.emptyList(); // If there is no valid mate entity, finish.

    int litterSize = (int) (Math.random() * Math.min(genetics.getMaxLitterSize(), mateEntity.genetics.getMaxLitterSize())) + 1;

    List<Animal> offsprings = new ArrayList<>();
    for (int i = 0; i < litterSize; i++) {
      AnimalGenetics childGenetics = genetics.breed(mateEntity.genetics);
      Vector newPos = position.getRandomPointInRadius(genetics.getMaxOffspringSpawnDistance());
      offsprings.add(createOffspring(childGenetics, newPos));
    }
    return offsprings;
  }

  /**
   * Checks if they have different genders and if the potential mate is alive and can multiply.
   * @param nearbyEntities The list of entities to search for a mate from.
   * @return A random mate from the list of entities, null if no mate found.
   */
  public Animal getRandomMate(List<Entity> nearbyEntities) {
    // Since nearbyEntities is already filtered by sight, it contains quite a few entities,
    // which doesn't affect performance as much.
    List<Entity> entities = getSameSpecies(searchNearbyEntities(nearbyEntities, genetics.getSight() * 0.5));
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