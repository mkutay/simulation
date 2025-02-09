package genetics;

import java.awt.*;

public class AnimalGenetics extends Genetics {
  private final int maxLitterSize; // Maximum number of offspring per breeding
  private final double mutationRate; // Rate at which the entity's genetics can mutate
  private final double maxSeed; // Max speed of the entity
  private final double sight; // Range at which the entity can see other entities
  private final Gender gender;
  private final String[] eats; // The names of the entities the animal eats
  private final double maxFoodLevel; // Maximum food level of the entity

  public AnimalGenetics(double multiplyingRate, int maxLitterSize, int maxAge, int matureAge, double mutationRate, double maxSeed, double sight, Gender gender, int size, String[] eats, String name, Color colour, double maxFoodLevel, int overcrowdingThreshold, double overcrowdingRadius, double maxOffspringSpawnDistance) {
    super(maxAge, matureAge, multiplyingRate, size, name, colour, overcrowdingThreshold, overcrowdingRadius, maxOffspringSpawnDistance);
    this.maxLitterSize = maxLitterSize;
    this.mutationRate = mutationRate;
    this.maxSeed = maxSeed;
    this.sight = sight;
    this.gender = gender;
    this.eats = eats;
    this.maxFoodLevel = maxFoodLevel;
  }

  public int getMaxLitterSize() { return maxLitterSize; }
  public double getMutationRate() { return mutationRate; }
  public double getMaxSpeed() { return maxSeed; }
  public double getSight() { return sight; }
  public Gender getGender() { return gender; }
  public String[] getEats() { return eats; }
  public double getMaxFoodLevel() { return maxFoodLevel; }

  /**
   * Breed with another animal to create a new animal with genetics in between the two parents.
   * TODO: THIS IS A CRUDE IMPLEMENTATION -- NEEDS TO BE IMPROVED.
   * TODO: ADD MUTATION.
   * TODO: Add a way to get the middle of the colour of the parents.
   * @param mate The other animal to breed with.
   * @return The genetics of the new animal.
   */
  public AnimalGenetics breed(AnimalGenetics mate) {
    double multiplyingRate = (this.getMultiplyingRate() + mate.getMultiplyingRate()) / 2;
    int maxLitterSize = (this.getMaxLitterSize() + mate.getMaxLitterSize()) / 2;
    int maxAge = (this.getMaxAge() + mate.getMaxAge()) / 2;
    int matureAge = (this.getMatureAge() + mate.getMatureAge()) / 2;
    double mutationRate = (this.getMutationRate() + mate.getMutationRate()) / 2;
    double maxSeed = (this.getMaxSpeed() + mate.getMaxSpeed()) / 2;
    double sight = (this.getSight() + mate.getSight()) / 2;
    Gender gender = Math.random() < 0.5 ? this.getGender() : mate.getGender();
    double maxFoodLevel = (this.getMaxFoodLevel() + mate.getMaxFoodLevel()) / 2;
    int overcrowdingThreshold = (this.getOvercrowdingThreshold() + mate.getOvercrowdingThreshold()) / 2;
    double overcrowdingRadius = (this.getOvercrowdingRadius() + mate.getOvercrowdingRadius()) / 2;
    int size = (this.getSize() + mate.getSize()) / 2;
    double maxOffspringSpawnDistance = (this.getMaxOffspringSpawnDistance() + mate.getMaxOffspringSpawnDistance()) / 2;
    return new AnimalGenetics(multiplyingRate, maxLitterSize, maxAge, matureAge, mutationRate, maxSeed, sight, gender, size, this.getEats(), this.getName(), this.getColour(), maxFoodLevel, overcrowdingThreshold, overcrowdingRadius, maxOffspringSpawnDistance);
  }
}