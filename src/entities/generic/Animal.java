package entities.generic;

import java.util.ArrayList;
import java.util.List;

import util.Utility;
import util.Vector;
import genetics.AnimalGenetics;
import simulation.Field;

public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; // Re-cast to AnimalGenetics
  protected int foodLevel;
  private double direction = Math.random() * Math.PI * 2;

  protected final AnimalEating eating;
  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
    this.foodLevel = genetics.getMaxFoodLevel();
    eating = new AnimalEating(this);
  }

  /**
   * Move the entity around randomly, bouncing off of the edges
   */
  public void wander(Field field, double deltaTime) {
    direction += (Math.random() - 0.5) * Math.PI * 0.1;
    if (field.isOutOfBounds(getPosition(), getSize())) {
      Vector centerOffset = field.getSize().multiply(0.5).subtract(getPosition());
      direction = centerOffset.getAngle() + (Math.random() - 0.5) * Math.PI;
    }
    double speed = getMaxSpeed() * 0.75 * deltaTime; //75% movespeed when wandering
    Vector movement = new Vector(Math.cos(direction) * speed, Math.sin(direction) * speed);
    position = getPosition().add(movement);
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

  public List<Animal> breed(List<Animal> others) {
    return null;
  }

  @Override
  public void update(Field field, double deltaTime) {
    super.update(field, deltaTime);
    wander(field, deltaTime);
  }

  // Getters:
  public String[] getEats() { return genetics.getEats(); }
  public double getMaxSpeed() { return genetics.getMaxSpeed(); }
}