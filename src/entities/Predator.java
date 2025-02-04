package entities;
import entities.generic.Animal;
import entities.generic.Entity;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Predator extends Animal {
  public Predator(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Draw the predator entity to the display as a square
   * @param display the display to draw to
   * @param scaleFactor the field scale factor for the position and size (for scaling screen size and simulation size)
   */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (genetics.getSize() / scaleFactor);
    int x = (int) ((position.x - size) / scaleFactor);  //Draw rectangle centered around x,y of predator
    int y = (int) ((position.y - size) / scaleFactor);

    display.drawRectangle(x, y, size*2, size*2, genetics.getColour());
  }

  /**
   * Update the entity in the field -- make all the actions it can.
   * Move, eat, multiply
   */
  @Override
  public void update(ArrayList<Entity> entities) {
    List<Entity> nearbyEntities = searchNearbyEntities(entities);

    List<Animal> newlyBornEntities = breed(getSameSpecies(nearbyEntities));
    // TODO: add newlyBornEntities to the field

    // TODO: move the entity

    // TODO: get all the entities that the this entity want to eat and can eat (colliding)
    // TODO: remove the entities from the field after running the following
    eat(null);
    checkFoodLevel();
  }
}
