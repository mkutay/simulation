package entities;
import java.util.ArrayList;

import entities.generic.*;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

public class Prey extends Animal {
  public Prey(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Draw the prey entity to the display as a circle.
   * @param display The display to draw to.
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
   */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (genetics.getSize() / scaleFactor);
    int x = (int) (position.x / scaleFactor);
    int y = (int) (position.y / scaleFactor);
    display.drawCircle(x, y, size, genetics.getColour());
  }

  /**
   * Update the entity in the field -- make all the actions it can.
   * Move, eat, multiply
   * TODO split update parts into seperate methods when needed
   */
  public void update(Field field, double deltaTime) {
    super.update(field, deltaTime);
    ArrayList<Entity> nearbyEntities = searchNearbyEntities(field.getEntities());

    for (Entity entity : nearbyEntities) {
      if (entity instanceof Plant plant) {
        // The plant may be dead in the current step, must check if dead first.
        if (plant.isAlive() && canEat(plant) && isColliding(plant)) {
          plant.setDead(); // Eat the plant.
        }
      }
    }

    // List<Animal> newlyBornEntities = breed(getSameSpecies(nearbyEntities));
    // TODO: add newlyBornEntities to the field

    // TODO: move the entity

    // TODO: get all the entities that the this entity want to eat and can eat (colliding)
    // TODO: remove the entities from the field after running the following
    // eat(null);
    // checkFoodLevel();
  }
}