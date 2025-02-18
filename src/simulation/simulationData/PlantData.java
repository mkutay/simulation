package simulation.simulationData;

import java.awt.Color;

import genetics.PlantGenetics;
import util.Utility;

/**
 * Represents a set of genetic intervals for a plant.
 * This is used when parsing the JSON file for type of plant.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class PlantData extends EntityData {
  public int[] numberOfSeeds; // Number of seeds produced by the plant -- when multiplied
  public double rainingGrowthFactor; // Growth factor when raining (affects number of seeds and range of growth)

  /**
   * @return A random set of genetics for a plant based on the data provided.
   */
  public PlantGenetics generateRandomGenetics() {
    Color convertedColour = new Color(this.colour[0], this.colour[1], this.colour[2]); // Convert rgRGB data to java.swing.Color
    Color mutatedColour = Utility.mutateColor(convertedColour, 1); // Change the colour slightly
    
    return new PlantGenetics(
      generateRandomNumberBetween(maxAge[0], maxAge[1]),
      generateRandomNumberBetween(matureAge[0], matureAge[1]),
      generateRandomNumberBetween(multiplyingRate[0], multiplyingRate[1]),
      generateRandomNumberBetween(size[0], size[1]),
      name,
      mutatedColour,
      generateRandomNumberBetween(numberOfSeeds[0], numberOfSeeds[1]),
      generateRandomNumberBetween(maxOffspringSpawnDistance[0], maxOffspringSpawnDistance[1]),
      generateRandomNumberBetween(overcrowdingThreshold[0], overcrowdingThreshold[1]),
      generateRandomNumberBetween(overcrowdingRadius[0], overcrowdingRadius[1]),
      generateRandomNumberBetween(mutationRate[0], mutationRate[1]),
      rainingGrowthFactor
    );
  }
}