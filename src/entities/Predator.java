package entities;

import entities.generic.*;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

import java.util.List;

/**
 * An arbitrary predator entity. Predators move around randomly and can reproduce.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Predator extends Animal {
  /**
   * Constructor -- Create a new predator entity with the given genetics and location.
   */
  public Predator(AnimalGenetics genetics, Vector location) {
    super(genetics, location);
  }

  /**
   * Updates the behaviour of the animal, specifically for movement.
   * @param field The field the animal is in.
   * @param nearbyEntities The entities in the sight radius of the animal.
   * @param deltaTime The delta time.
   */
  @Override
  protected void updateBehaviour(Field field, List<Entity> nearbyEntities, double deltaTime) {
    boolean isHungry = hungerController.isHungry();

    // if (!isHungry && field.isNight()) { return; }

    if (isHungry) hungerController.eat(nearbyEntities);

    boolean movingToFood = false;
    if (isHungry && !isMovingToMate) { // If is hungry and not currently attempting to mate.
      movingToFood = movementController.moveToNearestFood(nearbyEntities, deltaTime);
    }

    if (!movingToFood) { // If not moving to food and not hungry, look for mate.
      isMovingToMate = movementController.moveToNearestMate(nearbyEntities, deltaTime);
      if (!isMovingToMate) movementController.wander(field, deltaTime); // If can't find mate, wander.
    }
  }

  /**
   * Draw the predator entity to the display as a square.
   * @param display The display to draw to.
   * @param scaleFactor The field scale factor for the position and size (for scaling screen size and simulation size).
   */
  @Override
  public void draw(Display display, double scaleFactor) {
    int size = (int) (getCurrentVisualSize() / scaleFactor);
    size = Math.max(1, size);
    int x = (int) ((position.x() - (double) size / 2) / scaleFactor); // Draw rectangle centered around x, y of predator.
    int y = (int) ((position.y() - (double) size / 2) / scaleFactor);

    // drawSightRadius(display, scaleFactor);
    display.drawRectangle(x, y, size * 2, size * 2, genetics.getColour());
  }

  /**
   * Create a new predator entity with the given genetics and location.
   * Used for reproduction in the Animal class. Also used to avoid
   * code duplication.
   */
  @Override
  protected Animal createOffspring(AnimalGenetics genetics, Vector position) {
    return new Predator(genetics, position);
  }
}
