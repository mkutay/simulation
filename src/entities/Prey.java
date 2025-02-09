package entities;

import java.util.ArrayList;
import java.util.List;

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
    //display.drawCircle(x, y, (int) genetics.getSight(), Color.orange);
    display.drawCircle(x, y, size, genetics.getColour());
  }

  /**
   * Breeds with other entities of the same species, if possible.
   * @param others The list of entities to breed with.
   * @return A list of newly born entities.
   */
  public List<Prey> breed(Field field) {
    if (!canMultiply() || Math.random() > genetics.getMultiplyingRate()) return null;

    Prey mate = (Prey) getRandomMate(field.getEntities());
    if (mate == null) return null;
    // Clamp litter size to the minimum of the two animals' max litter size:
    int litterSize = (int) (Math.random() * Math.min(genetics.getMaxLitterSize(), mate.genetics.getMaxLitterSize())) + 1;

    List<Prey> newlyBornEntities = new ArrayList<>();
    for (int i = 0; i < litterSize; i++) {
      AnimalGenetics childGenetics = genetics.breed(mate.genetics);
      Vector newPos = position.getRandomPointInRadius(genetics.getMaxOffspringSpawnDistance());
      newlyBornEntities.add(new Prey(childGenetics, newPos));
    }
    return newlyBornEntities;
  }

  /**
   * Update the entity in the field -- make all the actions it can.
   * Move, eat, multiply.
   */
  @Override
  public void update(Field field, double deltaTime) {
    super.update(field, deltaTime);
    if (!isAlive()) return;

    List<Prey> newlyBornEntities = breed(field);
    if (newlyBornEntities == null) return;

    for (Prey entity : newlyBornEntities) {
      field.putInBounds(entity, entity.getSize());
      field.addEntity(entity);
    }

    foodLevel -= newlyBornEntities.size() * genetics.getMaxFoodLevel() * 0.1 * deltaTime;
  }
}