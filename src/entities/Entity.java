package entities;

import genetics.Genetics;

public class Entity {
  private String name; // Name of the entity
  private int age; // Age of the entity
  private boolean isAlive = true; // Whether the entity is alive or not
  
  protected Location location; // Location of the entity
  protected Genetics genetics; // Genetics of the entity

  public Entity(String name, Genetics genetics, Location location) {
    age = 0;
    this.name = name;
    this.genetics = genetics;
    this.location = location;
  }

  public Location getLocation() { return location; }
  public String getName() { return name; }
  public double getSize() { return genetics.getSize(); }

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
    return name + " at " + location;
  }
}