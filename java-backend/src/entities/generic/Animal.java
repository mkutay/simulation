package entities.generic;

import java.util.List;

import util.Vector;
import genetics.AnimalGenetics;
import simulation.Field;
import simulation.environment.Weather;
import simulation.simulationData.Data;

/**
 * Abstract class for all animals in the simulation. Contains controllers for
 * moving, breeding, handling hunger, and the behaviour of the animal. Contains 
 * the update method to handle all the controllers and update the behaviour of the animal.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; // Re-cast to AnimalGenetics. Stores the genetics of this animal.

  protected boolean isAsleep = false; // Stores if the animal is currently asleep.

  protected final AnimalMovementController movementController; // Controller for moving the animal.
  protected final AnimalBreedingController breedingController; // Controller for reproduction.
  protected final AnimalHungerController hungerController; // Controller for handling hunger.
  protected final AnimalBehaviourController behaviourController; // Controller for handling decision logic of animals.

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

    // Cache the nearby entities to avoid multiple searches over the field, which would slow down the simulation.
    List<Entity> nearbyEntities = searchNearbyEntities(field, genetics.getSight());
    
    // Delagate the breeding to the controller. If breeding occurs, add the offspring to the field,
    // otherwise the list will be empty.
    List<Animal> newEntities = breedingController.breed(nearbyEntities);
    for (Animal entity : newEntities) {
      field.putInBounds(entity, entity.getSize());
      field.addEntity(entity);
    }

    handleOvercrowding(nearbyEntities);

    hungerController.handleHunger(deltaTime, newEntities.size());

    windyCondition(field); // Handle windy condition.

    Vector lastPosition = position;
    movementController.setLastPosition(lastPosition); // Update last position before moving.

    // Update the behaviour of the animal, according to the nearby entities and the field:
    behaviourController.updateBehaviour(field, nearbyEntities, deltaTime);

    stormyCondition(field, lastPosition); // Handle stormy condition.
  }

  /**
   * Handle windy condition for the animal, moving it in the direction of the wind.
   * @param field The field the animal is in. Used to get access the environment.
   */
  private void windyCondition(Field field) {
    // If wind is present, move the animal in the direction of the wind.
    if (field.environment.getWeather() == Weather.WINDY || field.environment.getWeather() == Weather.STORM) {
      // Wind strength is applied here:
      Vector windVector = field.environment.getWindVector().multiply(Data.getWindStrength());
      this.setPosition(position.add(windVector));
    }
  }

  /**
   * Handle stormy condition for the animal, hindering its movement.
   * @param field The field the animal is in. Used to get access the environment.
   * @param lastPosition The last position of the animal to calculate the speed.
   */
  private void stormyCondition(Field field, Vector lastPosition) {
    // Hinder the speed of the animal if it is stormy:
    if (field.environment.getWeather() == Weather.STORM) {
      Vector differenceVector = position.subtract(lastPosition);
      double speed = differenceVector.getMagnitude();
      // The speed is decreased by the storm movement speed factor and the size of the animal:
      speed *= Data.getStormMovementSpeedFactor() / (getSize() / 4d);
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
  public boolean canEat(Entity entity) {
    return hungerController.canEat(entity);
  }
}