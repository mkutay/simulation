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
  protected int foodLevel;
  private double direction; // Currently facing direction for movement (Used to keep random movement natural looking)

  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
    this.foodLevel = genetics.getMaxFoodLevel();
    this.direction = Math.random() * Math.PI * 2;
  }

  /**
   * TODO: replace with boids-like simulation for cool herd movement? May be different for predators/prey
   * Simulates a simple wandering movement
   * Moves in the currently facing direction, while changing the facing direction randomly by a small amount
   */
  private void wander(Field field, double deltaTime) {
    direction += (Math.random() - 0.5) * Math.PI * 0.1;

    if (field.isOutOfBounds(position, getSize())) { // If out of bounds
      Vector centerOffset = field.getSize().multiply(0.5).subtract(position);
      direction = centerOffset.getAngle() + (Math.random() - 0.5) * Math.PI; // Point to center + random offset
    }

    double speed = genetics.getMaxSpeed() * 0.75 * deltaTime; // Moves at 75% of max speed when wandering
    Vector movement = new Vector(Math.cos(direction) * speed, Math.sin(direction) * speed);
    position = position.add(movement);
  }

  @Override
  public void update(Field field, double deltaTime) {
    super.update(field, deltaTime);
    wander(field, deltaTime);
  }

  /**
   * Checks if the animal can eat the entity.
   * @param entity The entity to check if it can be eaten.
   * @return True if the entity can be eaten, false otherwise.
   */
  public boolean canEat(Entity entity){
    return Arrays.asList(genetics.getEats()).contains(entity.getName());
  }

  /**
   * TODO: Optimise when necessary
   * @return Returns all entities in the field in the animals sight radius, except itself
   */
  public ArrayList<Entity> searchNearbyEntities(ArrayList<Entity> entities) {
    ArrayList<Entity> foundEntities = new ArrayList<>();
    if (entities == null) return foundEntities;

    for (Entity e : entities) {
      double distanceSquared = e.getPosition().subtract(position).getMagnitudeSquared();
      double sightRadius = genetics.getSight();
      // Epsilon is used for floating point comparison
      if (distanceSquared - sightRadius * sightRadius <= Utility.EPSILON && e != this) {
        // If can see other entity and it is not itself
        foundEntities.add(e);
      }
    }

    return foundEntities;
  }

  /**
   * Checks the food level of the animal and sets it to dead if it is less than or equal to 0.
   * Also updates the food level of the animal, as it decreases in each step.
   */
  protected void checkFoodLevel() {
    foodLevel -= 1;
    if (foodLevel <= 0) {
      setDead();
    }
  }

  /**
   * Breeds with other animals of the same species, if they are close enough.
   * @param others The other animals.
   * @return The newly born animals.
   */
  public List<Animal> breed(List<Animal> others) {
    return null;
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

  /**
   * Eats the entities and increases the food level of the animal
   * @param entities The entities that will be eaten
   */
  protected void eat(List<Entity> entities) {
    if (entities == null) return;
    entities.forEach(e -> foodLevel += e.getSize() * 3);
    foodLevel = Math.min(foodLevel, genetics.getMaxFoodLevel()); // Cap the foodLevel
  }

  // Getters:
  public String[] getEats() { return genetics.getEats(); }
  public double getMaxSpeed() { return genetics.getMaxSpeed(); }
}