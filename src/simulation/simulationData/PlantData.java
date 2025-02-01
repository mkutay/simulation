package simulation.simulationData;

import genetics.PlantGenetics;

/**
 * Represents a set of genetic intervals for a plant.
 * This is used when parsing the JSON file for type of plant.
 */
public class PlantData extends EntityData {
  public PlantGenetics generateRandomGenetics() {
    return new PlantGenetics(
      generateRandomNumberBetween(maxAge[0], maxAge[1]),
      generateRandomNumberBetween(matureAge[0], matureAge[1]),
      generateRandomNumberBetween(multiplyingRate[0], multiplyingRate[1]),
      generateRandomNumberBetween(size[0], size[1])
    );
  }
}