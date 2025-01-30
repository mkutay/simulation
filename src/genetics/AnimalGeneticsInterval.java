package genetics;

/**
 * Represents a set of genetic intervals for a given entity.
 * This is used when parsing the JSON file for each entity.
 */
public class AnimalGeneticsInterval extends GeneticsInterval {
  public int[] maxLitterSize; // Maximum number of offspring per breeding
  public int[] maxAge; // Maximum age of the entity
  public double[] matureAge; // Age at which the entity can start breeding
  public double[] mutationRate; // Rate at which the entity's genetics can mutate
  public double[] speed; // Speed of the entity
  public double[] sight; // Range at which the entity can see other entities
}