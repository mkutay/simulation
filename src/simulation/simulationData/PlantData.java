package simulation.simulationData;

import genetics.PlantGenetics;

import java.awt.Color;

/**
 * Represents a set of genetic intervals for a plant.
 * This is used when parsing the JSON file for type of plant.
 */
public class PlantData extends EntityData {
  /**
   * @return A random set of genetics for a plant based on the data provided.
   */
  public PlantGenetics generateRandomGenetics() {
    Color convertedColour = new Color(this.colour[0], this.colour[1], this.colour[2]); //Convert rgb data to java swing Color
    return new PlantGenetics(
      generateRandomNumberBetween(maxAge[0], maxAge[1]),
      generateRandomNumberBetween(matureAge[0], matureAge[1]),
      generateRandomNumberBetween(multiplyingRate[0], multiplyingRate[1]),
      generateRandomNumberBetween(size[0], size[1]),
      name,
      convertedColour
    );
  }
}