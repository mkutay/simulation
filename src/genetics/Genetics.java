package genetics;
public abstract class Genetics {
  private final int maxAge; // Maximum age of the entity
  private final int matureAge; // Age at which the entity can start breeding
  private final double multiplyingRate; // Rate at which the entity multiplies -- breads (animal) or spreads (plant)
  private final double size; // Size of the entity (the radius of the circle representing the entity)

  public Genetics(int maxAge, int matureAge, double multiplyingRate, double size) {
    this.maxAge = maxAge;
    this.matureAge = matureAge;
    this.multiplyingRate = multiplyingRate;
    this.size = size;
  }

  public int getMaxAge() { return maxAge; }
  public int getMatureAge() { return matureAge; }
  public double getMultiplyingRate() { return multiplyingRate; }
  public double getSize() { return size; }
}