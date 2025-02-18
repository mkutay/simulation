package entities.generic;

import genetics.AnimalGenetics;
import simulation.simulationData.Data;
import util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles the breeding logic for animals.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class AnimalBreedingController {
  private final Animal animal; // The animal this controller controls.

  /**
   * Constructor -- Create a new object.
   * @param animal The animal to control breeding for.
   */
  public AnimalBreedingController(Animal animal) {
    this.animal = animal;
  }

  /**
   * Breeds with a random mate (opposite gender) from the list of entities in sight.
   * @param nearbyEntities The list of entities in sight of this animal.
   * @return A list of offspring from the breeding, empty if no breeding occures.
   */
  public List<Animal> breed(List<Entity> nearbyEntities) {
    if (!canBreed() || Math.random() > animal.genetics.getMultiplyingRate()) {
      return Collections.emptyList();
    }

    Animal mateEntity = getRandomMate(nearbyEntities);
    if (mateEntity == null) return Collections.emptyList(); // If there is no valid mate entity, finish.

    double mateLitterSize = mateEntity.genetics.getMaxLitterSize();
    double animalLitterSize = animal.genetics.getMaxLitterSize();
    int litterSize = (int) (Math.random() * Math.min(animalLitterSize, mateLitterSize)) + 1;

    List<Animal> offsprings = new ArrayList<>();
    for (int i = 0; i < litterSize; i++) {
      AnimalGenetics childGenetics = animal.genetics.breed(mateEntity.genetics);
      Vector newPos = animal.position.getRandomPointInRadius(animal.genetics.getMaxOffspringSpawnDistance());
      offsprings.add(animal.createOffspring(childGenetics, newPos));
    }
    return offsprings;
  }

  /**
   * Checks if they have different genders and if the potential mate is alive and can multiply.
   * @param nearbyEntities The list of entities to search for a mate from.
   * @return A random mate from the list of entities, null if no mate found.
   */
  private Animal getRandomMate(List<Entity> nearbyEntities) {
    List<Entity> potentialMates = nearbyEntities.stream()
      .filter(this::canMateWith)
      .toList();

    if (potentialMates.isEmpty()) return null;
    return (Animal) potentialMates.get((int) (Math.random() * potentialMates.size()));
  }

  /**
   * @return True if this animal can mate with the specified entity, false otherwise.
   */
  protected boolean canMateWith(Entity entity) {
    if (entity instanceof Animal otherAnimal) {
      boolean isSameSpecies = otherAnimal.getName().equals(animal.getName());
      boolean isOppositeGender = otherAnimal.genetics.getGender() != animal.genetics.getGender();
      return isOppositeGender
        && isSameSpecies
        && otherAnimal.breedingController.canBreed()
        && canBreed();
    }
    return false;
  }

  /**
   * Animals can only breed if they have eaten food once in their life.
   * TODO: Minimum food requirement to control insane population growth.
   * @return True if this animal can breed/multiply, false otherwise.
   */
  private boolean canBreed() {
    return animal.canMultiply()
      && animal.hungerController.hasEaten()
      && animal.hungerController.getFoodLevel() >= Data.getAnimalBreedingCost();
  }
}