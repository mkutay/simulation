package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.generic.*;
import genetics.AnimalGenetics;
import graphics.Display;
import simulation.Field;
import util.Vector;

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
    int x = (int) ((position.x - (double) size / 2) / scaleFactor);  // Draw rectangle centered around x, y of predator.
    int y = (int) ((position.y - (double) size / 2) / scaleFactor);

    display.drawRectangle(x, y, size * 2, size * 2, genetics.getColour());
  }

  /**
   * Breeds with other entities of the same species, if possible.
   * @param others The list of entities to breed with.
   * @return A list of newly born entities.
   */
  public List<Predator> breed(Field field) {
    if (!isAlive() || !canMultiply() || Math.random() > genetics.getMultiplyingRate()) return Collections.emptyList();

    Entity mateEntity = getRandomMate(field.getEntities());
    if (!(mateEntity instanceof Predator)) return Collections.emptyList();
    Predator mate = (Predator) mateEntity;

    int litterSize = (int) (Math.random() * Math.min(genetics.getMaxLitterSize(), mate.genetics.getMaxLitterSize())) + 1;
    List<Predator> offspring = new ArrayList<>();
    for (int i = 0; i < litterSize; i++) {
      AnimalGenetics childGenetics = genetics.breed(mate.genetics);
      Vector newPos = position.getRandomPointInRadius(genetics.getMaxOffspringSpawnDistance());
      offspring.add(new Predator(childGenetics, newPos));
    }
    return offspring;
  }

  /**
   * Update the entity in the field -- make all the actions it can.
   * Move, eat, multiply.
   */
  @Override
  public void update(Field field, double deltaTime) {
    if (!isAlive()) return;
    
    List<Predator> newEntities = breed(field);
    
    for (Predator entity : newEntities) {
      field.putInBounds(entity, entity.getSize());
      field.addEntity(entity);
    }
    
    foodLevel -= newEntities.size() * genetics.getMaxFoodLevel() * 0.1 * deltaTime;

    super.update(field, deltaTime);
  }
}
