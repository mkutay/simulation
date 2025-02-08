package genetics;

import java.awt.Color;

public class PlantGenetics extends Genetics {
  private final int numberOfSeeds;
  private final double maxSeedSpawnDistance;

  public PlantGenetics(int maxAge, int matureAge, double spreadRate, int size, String name, Color colour, int numberOfSeeds, double maxSeedSpawnDistance) {
    super(maxAge, matureAge, spreadRate, size, name, colour);
    this.numberOfSeeds = numberOfSeeds;
    this.maxSeedSpawnDistance = maxSeedSpawnDistance;
  }

  public int getNumberOfSeeds() { return numberOfSeeds; }
  public double getMaxSeedSpawnDistance() { return maxSeedSpawnDistance; }
}
