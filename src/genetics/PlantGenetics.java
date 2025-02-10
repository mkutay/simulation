package genetics;

import java.awt.Color;

public class PlantGenetics extends Genetics {
  private final int numberOfSeeds;

  public PlantGenetics(int maxAge, int matureAge, double multiplyingRate, int size, String name, Color colour, int numberOfSeeds, double maxOffspringSpawnDistance, int overcrowdingThreshold, double overcrowdingRadius) {
    super(maxAge, matureAge, multiplyingRate, size, name, colour, overcrowdingThreshold, overcrowdingRadius, maxOffspringSpawnDistance);
    this.numberOfSeeds = numberOfSeeds;
  }

  public int getNumberOfSeeds() { return numberOfSeeds; }
}
