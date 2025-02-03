package entities.generic;

import entities.Vector;
import genetics.Genetics;

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

  public Vector getPosition() { return position; }
  public String getName() { return name; }
  public double getSize() { return genetics.getSize(); }
  public boolean isAlive() { return isAlive; }

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
  private void setDead() {
    isAlive = false;
  }
  
  public String toString() {
    return name + " at " + position.toString();
  }
}