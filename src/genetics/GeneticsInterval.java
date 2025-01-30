package genetics;

/**
 * Represents a set of genetic intervals for a given entity.
 * This is used when parsing the JSON file for each entity.
 */
public class GeneticsInterval {
  public String name;
  public double[] breedingProbability; // Probability of breeding each turn
}