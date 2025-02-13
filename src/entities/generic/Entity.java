package entities.generic;

import util.Utility;
import util.Vector;
import genetics.Genetics;
import graphics.Display;
import simulation.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class that holds the properties of an entity.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public abstract class Entity {
  private final String name; // Name of the entity
  private double age; // Age of the entity
  private boolean isAlive = true; // Whether the entity is alive or not
  protected Vector position; // Position of the entity
  protected Genetics genetics; // Genetics of the entity

  private final static double AGE_RATE = 1; //Controls how fast the creatures age

  /**
   * Constructor -- Create a new entity with the given genetics and position.
   */
  public Entity(Genetics genetics, Vector position) {
    age = 0;
    this.name = genetics.getName();
    this.genetics = genetics;
    this.position = position;
  }

  /**
   * Increment the age of the entity.
   * Age increase rate is proportional to delta time.
   */
  public void incrementAge(double deltaTime) {
    age += deltaTime * AGE_RATE;
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
   * Draw the entity to a display
   * @param display The display to draw to
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size)
   */
  public abstract void draw(Display display, double scaleFactor);

  /**
   * Update the entity.
   */
  public void update(Field field, double deltaTime) {
    incrementAge(deltaTime);
  }

  /**
   * @param entity The entity to check collision with self.
   * @return True if colliding with entity (uses circle hit box), false otherwise.
   */
  public boolean isColliding(Entity entity) {
    if (entity == null || entity == this) return false;
    Vector offset = position.subtract(entity.position);
    double distanceSquared = offset.getMagnitudeSquared();

    // This is mathematically the same as (distance <= (e.size + size)), but no sqrt call (in distance) for optimisation
    return distanceSquared <= Math.pow((getSize() + entity.getSize()), 2) + Utility.EPSILON;
  }

  /**
   * TODO: Optimise when necessary.
   * @param entities The entities that will be searched through.
   * @return Returns all entities in the field in the animals sight radius, except itself.
   */
  public List<Entity> searchNearbyEntities(List<Entity> entities, double searchRadius) {
    List<Entity> foundEntities = new ArrayList<>();
    if (entities == null) return foundEntities;

    for (Entity e : entities) {
      double distanceSquared = e.getPosition().subtract(position).getMagnitudeSquared();
      // Epsilon is used for floating point comparison.
      if (distanceSquared - searchRadius * searchRadius <= Utility.EPSILON && e != this) {
        foundEntities.add(e); // If can see other entity and it is not itself.
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
    // you have big brain
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
      if (entity.isAlive && entity.name.equals(name)) {
        sameSpecies.add(entity);
      }
    }
    return sameSpecies;
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
    isAlive = false;
  }

  /**
   * @return A string representation of the entity.
   */
  @Override
  public String toString() {
    return name + " at " + position.toString();
  }

  /**
   * Set position of the entity.
   */
  public void setPosition(Vector position) {
    if (position == null) return;
    this.position = position;
  }

  /**
   * Is 10% of the size at birth, grows to 100% size at mature age.
   * Minimum size is 2
   * Purely used for visuals, not for simulation calculations (not used for food values or collision)
   * @return the current size of the animal based on age
   */
  protected double getCurrentSize(){
    double birthSize = 0.1 * getSize();
    double size= Utility.lerp(birthSize, getSize(), Math.min(1.0, age / genetics.getMatureAge()));
    return Math.max(size, 2);
  }

  // Getters:
  public Vector getPosition() { return position; }
  public String getName() { return name; }
  public int getSize() { return genetics.getSize(); } //This getter is for code simplicity }
  public boolean isAlive() { return isAlive; }
}