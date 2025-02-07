package entities.generic;

import java.util.List;

public class AnimalBreeding {
  private Animal animal;

  public AnimalBreeding(Animal animal) {
    this.animal = animal;
  }

  /**
   * Breed with other animals of the same species.
   * @param others The other animals to breed with.
   * @return A list of new animals if breeding was successful, null otherwise.
   */
  public List<Animal> breed(List<Animal> others) {
    return null;
  }
}