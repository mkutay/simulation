package entities;

import genetics.Genetics;

public class Entity {
  private String name; // Name of the entity
  private int age; // Age of the entity
  private boolean isAlive = true; // Whether the entity is alive or not
  private Genetics genetics; // Genetics of the entity
  private Location location;

  public Entity(String name, Genetics genetics, Location location) {
    age = 0;
    this.name = name;
    this.genetics = genetics;
    this.location = location;
  }

  public Location getLocation() { return location; }
  public String getName() { return name; }

  public void incrementAge() {
    age++;
    if (age >= genetics.getMaxAge()) {
      setDead();
    }
  }

  protected boolean canMultiply() {
    return isAlive && age >= genetics.getMatureAge();
  }

  private void setDead() {
    isAlive = false;
  }
}