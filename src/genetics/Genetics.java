package genetics;
public class Genetics {
  private int maxAge; // Maximum age of the entity
  private int matureAge; // Age at which the entity can start breeding
  private double multiplyingRate; // Rate at which the entity multiplies -- breads (animal) or spreads (plant)

  public Genetics(int maxAge, int matureAge, double multiplyingRate) {
    this.maxAge = maxAge;
    this.matureAge = matureAge;
    this.multiplyingRate = multiplyingRate;
  }
}