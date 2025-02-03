package genetics;
public abstract class Genetics {
  private final String name; // Name of entity, acts as identifying key
  private final int maxAge; // Maximum age of the entity
  private final int matureAge; // Age at which the entity can start breeding
  private final double multiplyingRate; // Rate at which the entity multiplies -- breads (animal) or spreads (plant)
  private final int size; // Size of the entity (the radius of the circle representing the entity)
  private final int[] colour; // RGB colour of the entity

  public Genetics(int maxAge, int matureAge, double multiplyingRate, int size, String name, int[] colour) {
    this.maxAge = maxAge;
    this.matureAge = matureAge;
    this.multiplyingRate = multiplyingRate;
    this.size = size;
    this.name = name;
    this.colour = colour;
  }

  // Getters:
  public String getName() { return name; }
  public int getMaxAge() { return maxAge; }
  public int getMatureAge() { return matureAge; }
  public double getMultiplyingRate() { return multiplyingRate; }
  public int getSize() { return size; }
  public int[] getColour() { return colour; }
}