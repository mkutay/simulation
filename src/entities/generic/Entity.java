package entities.generic;

import util.Utility;
import util.Vector;
import genetics.Genetics;
import graphics.Display;
import simulation.Field;

public abstract class Entity {
  private final String name; // Name of the entity
  private int age; // Age of the entity
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
   * Increment the age of the entity by 1. Used when the simulation progresses by 1 step.
   * If the entity is older than or equal to the maximum age, the entity dies.
   */
  public void incrementAge() {
    age++;
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
    incrementAge();
  }

  /**
   * @param entity1 The first entity to check collision with
   * @param entity2 The second entity to check collision with
   * @return True if the entities are colliding (uses circle hit box), false otherwise
   */
  public static boolean isColliding(Entity entity1, Entity entity2) {
    if (entity1 == null || entity2 == null) return false;
    Vector offset = entity1.position.subtract(entity2.position);
    double distanceSquared = offset.getMagnitudeSquared();
    // This is mathematically the same as (distance <= (e.size + size)), but no sqrt call (in distance) for optimisation
    return distanceSquared <= Math.pow((entity1.getSize() + entity2.getSize()), 2) + Utility.EPSILON;
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