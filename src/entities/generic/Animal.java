package entities.generic;

import java.util.ArrayList;
import java.util.List;

import entities.Vector;
import genetics.AnimalGenetics;
import simulation.Field;

public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; // Re-cast to AnimalGenetics
  protected int foodLevel;
  private double direction; //Currently facing direction for movement (Used to keep random movement natural looking)

  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
    this.foodLevel = genetics.getMaxFoodLevel();
    this.direction = Math.random() * Math.PI * 2;
  }

  /**
   * TODO replace with boids-like simulation for cool herd movement? May be different for predators/prey
   * Simulates a simple wandering movement
   * Moves in the currently facing direction, while changing the facing direction randomly by a small amount
   */
  private void wander(double deltaTime){
    direction += (Math.random()-0.5) * Math.PI * 0.1;
    double speed = genetics.getMaxSpeed() * 0.75 * deltaTime; // Moves at 75% of max speed when wandering
    Vector movement = new Vector(Math.cos(direction) * speed, Math.sin(direction) * speed);
    position = position.add(movement);
  }

  @Override
  public void update(ArrayList<Entity> entities, double deltaTime) {
    wander(deltaTime);
  }

  /**
   * TODO: Optimise when necessary
   * TODO: Test this method -- create a test unit
   * @return Returns all entities in the field in the animals sight radius
   */
  public ArrayList<Entity> searchNearbyEntities(ArrayList<Entity> entities) {
    ArrayList<Entity> foundEntities = new ArrayList<>();

      for (Entity e : entities) {
      Vector entityPosition = e.getPosition();
      double distanceSquared = position.subtract(entityPosition).getMagnitudeSquared();
      double sightRadius = genetics.getSight();
      if (distanceSquared <= sightRadius * sightRadius && e != this) {
        // If can see other entity and it is not itself
        foundEntities.add(e);
      }
    }

    return foundEntities;
  }

  /*
   * Checks the food level of the animal and sets it to dead if it is less than or equal to 0
   * Also updates the food level of the animal, as it decreases in each step
   */
  protected void checkFoodLevel() {
    foodLevel -= 2;
    if (foodLevel <= 0) {
      setDead();
    }
  }
  
  public String[] getEats() { return genetics.getEats(); }
  public double getMaxSpeed() { return genetics.getMaxSpeed(); }

  public List<Animal> breed(List<Animal> others) {
    return new ArrayList<>();
  }

  /**
   * @param entities The entities that will be searched through
   * @return All animals of the same species as this animal
   */
  protected List<Animal> getSameSpecies(List<Entity> entities) {
    List<Animal> sameSpecies = new ArrayList<>();
    entities.forEach(entity -> {
      if (entity instanceof Animal && entity.getName().equals(getName())) {
        sameSpecies.add((Animal) entity);
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
}