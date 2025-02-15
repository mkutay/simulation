package genetics;

import java.awt.*;

import genetics.mutation.AnimalMutator;
import util.Utility;

/**
 * The genetics of an animal. Contains information about the animal's breeding,
 * mutation, and other properties. Also allows for breeding with another animal, for 
 * creating a new animal with genetics in between the two parents.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class AnimalGenetics extends Genetics {
  private final int maxLitterSize; // Maximum number of offspring per breeding
  private final double maxSpeed; // Max speed of the entity
  private final double sight; // Range at which the entity can see other entities
  private final Gender gender; // The gender of the animal
  private final String[] eats; // The names of the entities the animal eats

  /**
   * Constructor -- Creates a new set of genetics for an animal.
   */
  public AnimalGenetics(double multiplyingRate, int maxLitterSize, int maxAge, int matureAge, double mutationRate, double maxSpeed, double sight, Gender gender, int size, String[] eats, String name, Color colour, int overcrowdingThreshold, double overcrowdingRadius, double maxOffspringSpawnDistance) {
    super(maxAge, matureAge, multiplyingRate, size, name, colour, overcrowdingThreshold, overcrowdingRadius, maxOffspringSpawnDistance, mutationRate);
    this.maxLitterSize = maxLitterSize;
    this.maxSpeed = maxSpeed;
    this.sight = sight;
    this.gender = gender;
    this.eats = eats;
  }

  // Getters:
  public int getMaxLitterSize() { return maxLitterSize; }
  public double getMaxSpeed() { return maxSpeed; }
  public double getSight() { return sight; }
  public Gender getGender() { return gender; }
  public String[] getEats() { return eats; }

  /**
   * Breed with another animal to create a new animal with genetics in between the two parents.
   * @param mate The other animal to breed with.
   * @return The genetics of the new animal.
   */
  public AnimalGenetics breed(AnimalGenetics mate) {
    Gender gender = Math.random() < 0.5 ? this.getGender() : mate.getGender();
    return AnimalMutator.mutateAnimalGenetics(new AnimalGenetics(
      singleBreed(this.getMultiplyingRate(), mate.getMultiplyingRate()),
      singleBreed(this.getMaxLitterSize(), mate.getMaxLitterSize()),
      singleBreed(this.getMaxAge(), mate.getMaxAge()),
      singleBreed(this.getMatureAge(), mate.getMatureAge()),
      singleBreed(this.getMutationRate(), mate.getMutationRate()),
      singleBreed(this.getMaxSpeed(), mate.getMaxSpeed()),
      singleBreed(this.getSight(), mate.getSight()),
      gender,
      singleBreed(this.getSize(), mate.getSize()),
      this.getEats(),
      this.getName(),
      Utility.breedColor(this.getColour(), mate.getColour()),
      singleBreed(this.getOvercrowdingThreshold(), mate.getOvercrowdingThreshold()),
      singleBreed(this.getOvercrowdingRadius(), mate.getOvercrowdingRadius()),
      singleBreed(this.getMaxOffspringSpawnDistance(), mate.getMaxOffspringSpawnDistance())
    ));
  }

  private double singleBreed(double value, double mateValue) {
    double r = Math.random();
    return r * value + (1 - r) * mateValue;
  }

  private int singleBreed(int value, int mateValue) {
    double r = Math.random();
    return (int) Math.round(r * value + (1 - r) * mateValue);
  }
}