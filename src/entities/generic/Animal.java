package entities.generic;

import java.util.List;

import util.Vector;
import genetics.AnimalGenetics;
import simulation.Field;
import simulation.Weather;
import simulation.simulationData.Data;

/**
 * Abstract class for all animals in the simulation.
 * Contains controllers for moving, breeding, and handling hunger.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; // Re-cast to AnimalGenetics

  protected boolean isMovingToMate = false; // Stores if the animal is currently attempting to mate
  protected boolean isAsleep = false;

  protected final AnimalMovementController movementController; // Controller for moving the animal
  protected final AnimalBreedingController breedingController; // Controller for breeding the animal
  protected final AnimalHungerController hungerController; // Controller for handling hunger
  protected final AnimalBehaviourController behaviourController; // Controller for handling decision logic of animals

  /**
   * Constructor -- Create a new Animal.
   * @param genetics The genetics of the animal.
   * @param position The position of the animal.
   */
  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
    movementController = new AnimalMovementController(this, position);
    breedingController = new AnimalBreedingController(this);
    hungerController = new AnimalHungerController(this);
    behaviourController = new AnimalBehaviourController(this);
  }

  /**
   * Update the animal. Handle all the controllers and update the behaviour.
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

    if (field.environment.getWeather() == Weather.WINDY || field.environment.getWeather() == Weather.STORM) {
      Vector windVector = field.environment.getWindVector().multiply(Data.getWindStrength());
      setPosition(position.add(windVector));
    }

    Vector lastPosition = position;
    movementController.setLastPosition(lastPosition); // Update last position before moving.
    behaviourController.updateBehaviour(field, nearbyEntities, deltaTime);

    if (field.environment.getWeather() == Weather.STORM) {
      Vector differenceVector = position.subtract(lastPosition);
      double speed = differenceVector.getMagnitude();
      speed *= Data.getStormMovementSpeedFactor();
      setPosition(lastPosition.add(differenceVector.multiply(speed)));
    }
  }

  /**
   * Used to generate an offspring of the animal after breeding.
   * @return A new Animal with the specified genetics and spawn position.
   */
  protected abstract Animal createOffspring(AnimalGenetics genetics, Vector position);

  /**
   * Used for a rather specific use case in prey detecting predators that can eat them,
   * so that the prey can run away.
   */
  public AnimalHungerController getHungerController() {
    return hungerController;
  }
}