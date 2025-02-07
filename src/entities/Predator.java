package entities;
import entities.generic.Animal;
import entities.generic.Entity;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

import java.util.ArrayList;
import java.util.List;

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

    List<Entity> nearbyEntities = searchNearbyEntities(field.getEntities());
    List<Entity> wantToEat = new ArrayList<>();

    for (Entity entity : nearbyEntities) {
      if (entity instanceof Prey prey) {
        // The prey may be dead in the current step, must check if dead first.
        if (prey.isAlive() && canEat(prey) && isColliding((prey))) {
          wantToEat.add(prey);
        }
      }
    }

    // Eat all entities in wantToEat and check the food level of the predator.
    eat(wantToEat);
    checkFoodLevel();

    for (Entity prey : wantToEat) {
      prey.setDead();
    }

    List<Animal> newlyBornEntities = breed(getSameSpecies(nearbyEntities));
    if (newlyBornEntities != null) for (Animal entity : newlyBornEntities) {
      field.putInBounds(entity, entity.getSize());
      field.addEntity(entity);
    }
  }
}
