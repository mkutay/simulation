package entities.generic;

import simulation.Field;
import util.Vector;

public class AnimalMovement {
  private double direction;
  private Animal animal;

  public AnimalMovement(Animal animal) {
    this.animal = animal;
    direction = Math.random() * Math.PI * 2;
  }

  public void simpleWander(Field field, double deltaTime) {
    direction += (Math.random() - 0.5) * Math.PI * 0.1;
    if (field.isOutOfBounds(animal.getPosition(), animal.getSize())) {
      Vector centerOffset = field.getSize().multiply(0.5).subtract(animal.getPosition());
      direction = centerOffset.getAngle() + (Math.random() - 0.5) * Math.PI;
    }
    double speed = animal.getMaxSpeed() * 0.75 * deltaTime;
    Vector movement = new Vector(Math.cos(direction) * speed, Math.sin(direction) * speed);
    animal.position = animal.getPosition().add(movement);
  }
}
