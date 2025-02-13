package simulation.simulationData;

import java.awt.Color;

import genetics.AnimalGenetics;
import genetics.Gender;

/**
 * Represents a set of genetic intervals for an animal.
 * This is used when parsing the JSON file for each species of animals.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class AnimalData extends EntityData{
  public int[] maxLitterSize; // Maximum number of offspring per breeding
  public double[] mutationRate; // Rate at which the entity's genetics can mutate
  public double[] maxSpeed; // Speed of the entity
  public double[] sight; // Range at which the entity can see other entities
  public String[] eats; // List of entities that this entity can eat
  public double[] maxFoodLevel; // Maximum food level of the entity

  /**
   * @return A random set of genetics for an animal based on the data provided.
   */
  public AnimalGenetics generateRandomGenetics() {
    Color convertedColour = new Color(this.colour[0], this.colour[1], this.colour[2]); // Convert RGB data to java.swing.Color
    
    return new AnimalGenetics(
      generateRandomNumberBetween(multiplyingRate[0], multiplyingRate[1]),
      generateRandomNumberBetween(maxLitterSize[0], maxLitterSize[1]),
      generateRandomNumberBetween(maxAge[0], maxAge[1]),
      generateRandomNumberBetween(matureAge[0], matureAge[1]),
      generateRandomNumberBetween(mutationRate[0], mutationRate[1]),
      generateRandomNumberBetween(maxSpeed[0], maxSpeed[1]),
      generateRandomNumberBetween(sight[0], sight[1]),
      getRandomGender(), // Random gender
      generateRandomNumberBetween(size[0], size[1]),
      eats,
      name,
      convertedColour,
      generateRandomNumberBetween(overcrowdingThreshold[0], overcrowdingThreshold[1]),
      generateRandomNumberBetween(overcrowdingRadius[0], overcrowdingRadius[1]),
      generateRandomNumberBetween(maxOffspringSpawnDistance[0], maxOffspringSpawnDistance[1])
    );
  }

  public Gender getRandomGender() {
    return Math.random() <= 0.5 ? Gender.MALE : Gender.FEMALE;
  }
}