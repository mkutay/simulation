package entities;
import entities.generic.Animal;
import entities.generic.Entity;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;

import java.awt.*;
import java.util.List;

public class Prey extends Animal {
  public Prey(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Draw the entity to a display
   * @param display the display to draw to
   */
  @Override
  public void draw(Display display) {
    display.drawCircle((int) position.x, (int) position.y, genetics.getSize(), Color.BLUE);
  }

  /**
   * Update the entity in the field -- make all the actions it can.
   * Move, eat, multiply
   */
  public void update(Field field) {
    List<Entity> nearbyEntities = searchNearbyEntities(field);

    List<Animal> newlyBornEntities = breed(getSameSpecies(nearbyEntities));
    // TODO: add newlyBornEntities to the field

    // TODO: move the entity

    // TODO: get all the entities that the this entity want to eat and can eat (colliding)
    // TODO: remove the entities from the field after running the following
    eat(null);
  }
}