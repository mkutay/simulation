package simulation.simulationData;

/**
 * Represents a set of genetic intervals for an entity.
 * This contains all the data that are both in animals and plants.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public abstract class EntityData {
  public String name; // Name of the entity.
  public int numberOfEntitiesAtStart; // Number of entities at the start of the simulation.
  public int[] maxAge; // Maximum age of the entity.
  public int[] matureAge; // Age at which the entity can start breeding.
  public double[] multiplyingRate; // Rate at which the entity can multiply.
  public int[] size; // Size of the entity.
  public int[] colour; // RGB colour of the entity.
  public int[] overcrowdingThreshold; // Number of entities at which the entity will die.
  public double[] overcrowdingRadius; // Radius inside of which the threshold is checked.
  public double[] maxOffspringSpawnDistance; // Maximum distance offspring can spawn from the parent entity.
  public double[] mutationRate; // The rate at which the genetics will mutate.

  /**
   * @return A random number between the given double min and max values.
   */
  protected double generateRandomNumberBetween(double min, double max) {
    return min + Math.random() * (max - min);
  }

  /**
   * @return A random number between the given int min and max values.
   */
  protected int generateRandomNumberBetween(int min, int max) {
    return (int) (min + Math.random() * (max - min));
  }
}
