package genetics;

/**
 * Represents a set of genetic intervals for a plant.
 * This is used when parsing the JSON file for type of plant.
 */
public class PlantData extends EntityData {
  public double[] spreadRate; // Probability of spreading each turn

  public PlantGenetics generateRandomGenetics() {
    return new PlantGenetics(
      generateRandomNumberBetween(spreadRate[0], spreadRate[1])
    );
  }
}