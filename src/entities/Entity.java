package entities;
public class Entity {
  private String name; // Name of the entity
  private int age; // Age of the entity
  boolean isAlive = true; // Whether the entity is alive or not

  public Entity(int age, String name) {
    this.age = age;
    this.name = name;
  }
}