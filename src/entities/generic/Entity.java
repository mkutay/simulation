package entities.generic;

import util.Utility;
import util.Vector;
import genetics.Genetics;
import graphics.Display;
import simulation.Field;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
  private final String name; // Name of the entity
  private double age; // Age of the entity
  private boolean isAlive = true; // Whether the entity is alive or not
  protected Vector position; // Position of the entity
  protected Genetics genetics; // Genetics of the entity

  public Entity(Genetics genetics, Vector position) {
    age = 0;
    this.name = genetics.getName();
    this.genetics = genetics;
    this.position = position;
  }

  /**
   * Increment the age of the entity.
   * Age increase rate is proportional to delta time
   * If the entity is older than or equal to the maximum age, the entity dies.
   */
  public void incrementAge(double deltaTime) {
    age += deltaTime;
    if (age >= genetics.getMaxAge()) {
      setDead();
    }
  }

  /**
   * @return Whether the entity can multiply or not, according to the mature age.
   */
  protected boolean canMultiply() {
    return isAlive && age >= genetics.getMatureAge();
  }

  /**
   * Set the entity as dead.
   */
  public void setDead() {
    isAlive = false;
  }

  /**
   * Draw the entity to a display
   * @param display The display to draw to
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size)
   */
  public abstract void draw(Display display, double scaleFactor);

  public void update(Field field, double deltaTime) {
    if (!isAlive) return;
    incrementAge(deltaTime);
    handleOvercrowding(field);
  }

  /**
   * @param entity The entity to check collision with self
   * @return True if colliding with entity (uses circle hit box), false otherwise
   */
  public boolean isColliding(Entity entity) {
    if (entity == null) return false;
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
   * If there are more than 2 entities of the same species in the vicinity, the entity dies.
   */
  public void handleOvercrowding(Field field) {
    List<Entity> entities = searchNearbyEntities(field.getEntities(), genetics.getOvercrowdingRadius());
    List<Entity> sameSpecies = getSameSpecies(entities).stream().filter(entity -> entity.isAlive()).toList();
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
    entities.forEach(entity -> {
      if (entity.getName().equals(getName())) {
        sameSpecies.add(entity);
      }
    });
    return sameSpecies;
  }

  public void setAge(double age) {
    this.age = age;
  }
    
  public String toString() {
    return name + " at " + position.toString();
  }

  // Getters:
  public Vector getPosition() { return position; }
  public String getName() { return name; }
  public int getSize() { return genetics.getSize(); }
  public boolean isAlive() { return isAlive; }
}