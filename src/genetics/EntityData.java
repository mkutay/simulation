package genetics;

public class EntityData {
  public String name;
  public int numberOfEntitiesAtStart; // Number of entities at the start of the simulation
  public int[] maxAge;
  public int[] matureAge;
  public double[] multiplyingRate;
  public double[] size;

  protected double generateRandomNumberBetween(double min, double max) {
    return min + Math.random() * (max - min);
  }

  protected int generateRandomNumberBetween(int min, int max) {
    return (int) (min + Math.random() * (max - min));
  }
}
