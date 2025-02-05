package entities;
import entities.generic.Animal;
import entities.generic.Entity;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

import java.util.ArrayList;

public class Predator extends Animal {
  public Predator(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Draw the predator entity to the display as a square.
   * @param display The display to draw to.
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
   */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (genetics.getSize() / scaleFactor);
    int x = (int) ((position.x - (double) size / 2) / scaleFactor);  //Draw rectangle centered around x,y of predator
    int y = (int) ((position.y - (double) size / 2) / scaleFactor);

    display.drawRectangle(x, y, size * 2, size * 2, genetics.getColour());
    // display.drawCircle((int) (position.x / scaleFactor), (int) (position.y / scaleFactor), size, Color.ORANGE);
  }

  /**
   * Update the entity in the field -- make all the actions it can.
   * Move, eat, multiply
   * TODO: split update parts into seperate methods when needed
   */
  @Override
  public void update(Field field, double deltaTime) {
    super.update(field, deltaTime);

    ArrayList<Entity> nearbyEntities = searchNearbyEntities(field.getEntities());

    for (Entity entity : nearbyEntities) {
      if (entity instanceof Prey prey) {
        // The prey may be dead in the current step, must check if dead first.
        if (prey.isAlive() && canEat(prey) && isColliding((prey))) {
          prey.setDead(); // Eat the prey.
        }
      }
    }

    // List<Animal> newlyBornEntities = breed(getSameSpecies(nearbyEntities));
    // TODO: add newlyBornEntities to the field

    // TODO: get all the entities that the this entity want to eat and can eat (colliding)
    // TODO: remove the entities from the field after running the following
    // eat(null);
    // checkFoodLevel();
  }
}
