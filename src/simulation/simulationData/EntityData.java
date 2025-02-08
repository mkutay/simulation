package simulation.simulationData;

public class EntityData {
  public String name; // Name of the entity
  public int numberOfEntitiesAtStart; // Number of entities at the start of the simulation
  public int[] maxAge; // Maximum age of the entity
  public int[] matureAge; // Age at which the entity can start breeding
  public double[] multiplyingRate; // Rate at which the entity can multiply
  public int[] size; // Size of the entity
  public int[] colour; // RGB colour of the entity
  public int[] overcrowdingThreshold; // Number of entities at which the entity will die
  public double[] overcrowdingRadius; // Radius inside of which the threshold is checked

  protected double generateRandomNumberBetween(double min, double max) {
    return min + Math.random() * (max - min);
  }

  protected int generateRandomNumberBetween(int min, int max) {
    return (int) (min + Math.random() * (max - min));
  }
}
