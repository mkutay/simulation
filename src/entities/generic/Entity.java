package entities.generic;

import util.Utility;
import util.Vector;
import genetics.Genetics;
import graphics.Display;
import simulation.Field;
import simulation.simulationData.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class that holds the properties of an entity.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public abstract class Entity {
  protected Vector position; // Position of the entity
  protected Genetics genetics; // Genetics of the entity
  private double age = 0; // Age of the entity
  private boolean isAlive = true; // Whether the entity is alive or not

  /**
   * Constructor -- Create a new entity with the given genetics and position.
   */
  public Entity(Genetics genetics, Vector position) {
    this.genetics = genetics;
    this.position = position;
  }

  /**
   * Check if the entity is colliding with another entity.
   * @param entity The entity to check collision with self.
   * @return True if colliding with entity (uses circle hit box), false otherwise or if entitiy is this.
   */
  public boolean isColliding(Entity entity) {
    if (entity == null || entity == this) return false;
    double distanceSquared = this.position.subtract(entity.position).getMagnitudeSquared();

    // This is mathematically the same as (distance <= (e.size + size)), but no sqrt call for optimisation.
    int sumOfSizes = this.getSize() + entity.getSize();
    return distanceSquared <= sumOfSizes * sumOfSizes + Utility.EPSILON;
  }

  /**
   * TODO: Optimise when necessary.
   * Search for entities in the search radius.
   * @param entities The entities that will be searched through.
   * @param searchRadius The radius to search for entities.
   * @return Returns all entities in the field in the radius, except itself.
   */
  public List<Entity> searchNearbyEntities(List<Entity> entities, double searchRadius) {
    List<Entity> foundEntities = new ArrayList<>();
    if (entities == null) return foundEntities;

    for (Entity e : entities) {
      double distanceSquared = e.getPosition().subtract(position).getMagnitudeSquared();
      // Epsilon is used for floating point comparison.
      if (e != this && distanceSquared - searchRadius * searchRadius <= Utility.EPSILON) {
        foundEntities.add(e);
      }
    }

    return foundEntities;
  }

  /**
   * Handles overcrowding of entities of the same species.
   * Looks at the genetics of the species to determine overcrowding.
   * @param nearbyEntities The entities in the sight radius of this entity.
   */
  public void handleOvercrowding(List<Entity> nearbyEntities) {
    // Since nearbyEntities was already filtered by sight radius, this method
    // call doesn't affect performance as much.
    // Additionally, overcrowdingRadius should be less than sight radius, so this is needed.
    List<Entity> entities = searchNearbyEntities(nearbyEntities, genetics.getOvercrowdingRadius());
    List<Entity> sameSpecies = getSameSpecies(entities);
    if (sameSpecies.size() >= genetics.getOvercrowdingThreshold()) {
      setDead();
    }
  }

  /**
   * Used for breeding and overcrowding.
   * @param entities The entities that will be searched through.
   * @return All entities of the same species as this entity.
   */
  protected List<Entity> getSameSpecies(List<Entity> entities) {
    List<Entity> sameSpecies = new ArrayList<>();
    for (Entity entity : entities) {
      if (entity.isAlive && entity.getName().equals(this.getName())) {
        sameSpecies.add(entity);
      }
    }
    return sameSpecies;
  }

  /**
   * Is 10% of the size at birth, grows to 100% size at mature age. Minimum size is two.
   * Purely used for visuals, not for simulation calculations (food values nor collision).
   * @return The current visual size of the animal based on age.
   */
  protected double getCurrentVisualSize() {
    double birthSize = 0.1 * getSize();
    double size = Utility.lerp(birthSize, getSize(), Math.min(1.0, age / genetics.getMatureAge()));
    return Math.max(size, 2);
  }

  /**
   * Increment the age of the entity.
   * Age increase rate is proportional to delta time.
   * @param deltaTime The time passed since the last update.
   */
  public void incrementAge(double deltaTime) {
    age += deltaTime * Data.getEntityAgeRate();
    if (age >= genetics.getMaxAge()) {
      setDead();
    }
  }

  /**
   * @return Whether the entity can reproduce or not, according to the mature age.
   */
  protected boolean canMultiply() {
    return isAlive && age >= genetics.getMatureAge() && age < genetics.getMaxAge();
  }

  /**
   * Draw the entity to a display.
   * @param display The display to draw to.
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
   */
  public abstract void draw(Display display, double scaleFactor);

  /**
   * Update the entity.
   */
  public void update(Field field, double deltaTime) {
    incrementAge(deltaTime);
  }

  /**
   * Set age of the entity.
   */
  public void setAge(double age) {
    this.age = age;
  }

  /**
   * Set the entity as dead.
   */
  public void setDead() {
    this.isAlive = false;
  }

  /**
   * @return A string representation of the entity.
   */
  @Override
  public String toString() {
    return this.getName() + " at " + this.position.toString();
  }

  /**
   * Set position of the entity.
   */
  public void setPosition(Vector position) {
    if (position != null) this.position = position;
  }

  // Getters:
  public Vector getPosition() { return position; }
  public String getName() { return genetics.getName(); }
  public int getSize() { return genetics.getSize(); } //This getter is for code simplicity }
  public boolean isAlive() { return isAlive; }
}