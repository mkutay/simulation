package entities.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.Utility;
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
   * Move the entity around randomly, bouncing off of the edges
   */
  public void wander(Field field, double deltaTime) {
    direction += (Math.random() - 0.5) * Math.PI * 0.1;
    if (field.isOutOfBounds(getPosition(), getSize())) {
      Vector centerOffset = field.getSize().multiply(0.5).subtract(getPosition());
      direction = centerOffset.getAngle() + (Math.random() - 0.5) * Math.PI;
    }
    double speed = getMaxSpeed() * 0.75 * deltaTime; //75% movespeed when wandering
    Vector movement = new Vector(Math.cos(direction) * speed, Math.sin(direction) * speed);
    position = getPosition().add(movement);
  }

  /**
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

  public boolean canEat(Entity entity) {
    return Arrays.asList(getEats()).contains(entity.getName());
  }

  /**
   * Attempts to eat any colliding entities
   * @param nearbyEntities the entities in the sight radius of this animal
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

    foodLevel = Math.min(foodLevel, genetics.getMaxFoodLevel()); //Clamp food from exceeding max food of animal
  }

  /**
   * Sets animal to die if no food, decrements food level proportion to deltaTime.
   * TODO food level decrements based on speed.
   * TODO food level decrements when reproducing
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
    wander(field, deltaTime);
    handleHunger(deltaTime);
  }

  // Getters:
  public String[] getEats() { return genetics.getEats(); }
  public double getMaxSpeed() { return genetics.getMaxSpeed(); }
}