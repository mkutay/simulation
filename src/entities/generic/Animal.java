package entities.generic;

import java.util.List;

import util.Vector;
import genetics.AnimalGenetics;
import simulation.Field;

/**
 * Abstract class for all animals in the simulation.
 * Contains methods for moving, eating, breeding, and handling hunger.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; // Re-cast to AnimalGenetics

  protected boolean isMovingToMate = false; // Stores if the animal is currently attempting to mate

  protected final AnimalMovementController movementController;
  protected final AnimalBreedingController breedingController;
  protected final AnimalHungerController hungerController;

  /**
   * Constructor -- Create a new Animal.
   * @param genetics The genetics of the animal.
   * @param position The position of the animal.
   */
  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    movementController = new AnimalMovementController(this, position);
    breedingController = new AnimalBreedingController(this);
    hungerController = new AnimalHungerController(this);
    this.genetics = genetics;
  }

  /**
   * Update the animal.
   */
  @Override
  public void update(Field field, double deltaTime) {
    if (!isAlive()) return;
    super.update(field, deltaTime);
    
    List<Entity> nearbyEntities = searchNearbyEntities(field, genetics.getSight());
    
    List<Animal> newEntities = breedingController.breed(nearbyEntities);
    for (Animal entity : newEntities) {
      field.putInBounds(entity, entity.getSize());
      field.addEntity(entity);
    }

    handleOvercrowding(field);

    hungerController.handleHunger(deltaTime, newEntities.size());

    movementController.setLastPosition(position); //Update last position before moving
    updateBehaviour(field, nearbyEntities, deltaTime);
  }

  /**
   * Updates the behaviour of the animal, specifically for movement.
   * @param field The field the animal is in.
   * @param nearbyEntities The entities in the sight radius of the animal.
   * @param deltaTime The delta time.
   */
  protected abstract void updateBehaviour(Field field, List<Entity> nearbyEntities, double deltaTime);

  /**
   * Used to generate an offspring of the animal after breeding.
   * @return A new Animal with the specified genetics and spawn position.
   */
  protected abstract Animal createOffspring(AnimalGenetics genetics, Vector position);

  public AnimalHungerController getHungerController() { //for a rather specific use case in prey detecting predators that can eat them
    return hungerController;
  }
}