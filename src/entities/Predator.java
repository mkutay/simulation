package entities;
import entities.generic.Animal;
import entities.generic.Entity;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;

import java.awt.*;
import java.util.List;

public class Predator extends Animal {
  public Predator(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Draw the predator entity to the display as a square
   * @param display the display to draw to
   */
  @Override
  public void draw(Display display) {
    int size = genetics.getSize()*2;
    int x = (int) position.x - size/2; //Draw rectangle centered around x,y of predator
    int y = (int) position.y - size/2;
    display.drawRectangle(x, y, size, size, genetics.getColour());
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
    checkFoodLevel();
  }
}
