package entities.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.Vector;
import genetics.AnimalGenetics;
import simulation.Field;

public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; // Re-cast to AnimalGenetics
  protected double foodLevel;
  private double direction = Math.random() * Math.PI * 2;

  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
    this.foodLevel = genetics.getMaxFoodLevel();
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
        }
      }
    }

    if (nearestEntity == null) return false;
    
    moveToEntity(nearestEntity, deltaTime);
    return true;
  }

  /**
   * Used for breeding.
   * @param entities The entities that will be searched through.
   * @return All animals of the same species as this animal.
   */
  protected List<Animal> getSameSpecies(List<Entity> entities) {
    List<Animal> sameSpecies = new ArrayList<>();
    entities.forEach(entity -> {
      if (entity instanceof Animal animal && animal.getName().equals(getName())) {
        sameSpecies.add(animal);
      }
    });
    return sameSpecies;
  }

  /**
   * Checks the "genetics.eats" of this animal.
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
      // The entity may be dead in the current step, must check if dead first.
      if (entity.isAlive() && canEat(entity) && isColliding(entity)) {
        foodLevel += entity.getSize() * 3;
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
    foodLevel -= genetics.getMaxFoodLevel() * 0.01 * deltaTime;
    if (foodLevel <= 0) {
      setDead();
    }
  }

  public List<Animal> breed(List<Animal> others) {
    return null;
  }

  @Override
  public void update(Field field, double deltaTime) {
    super.update(field, deltaTime);
    List<Entity> nearbyEntities = searchNearbyEntities(field.getEntities(), genetics.getSight());

    boolean movingToFood = moveToNearestFood(nearbyEntities, deltaTime);
    if (!movingToFood) wander(field, deltaTime);

    eat(nearbyEntities);
    handleHunger(deltaTime);

    List<Animal> newlyBornEntities = breed(getSameSpecies(nearbyEntities));
    if (newlyBornEntities != null) for (Animal entity : newlyBornEntities) {
      field.putInBounds(entity, entity.getSize());
      field.addEntity(entity);
    }
  }

  // Getters:
  public String[] getEats() { return genetics.getEats(); }
  public double getMaxSpeed() { return genetics.getMaxSpeed(); }
}