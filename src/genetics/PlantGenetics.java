package genetics;

import java.awt.Color;

public class PlantGenetics extends Genetics {
  int numberOfSeeds;

  public PlantGenetics(int maxAge, int matureAge, double spreadRate, int size, String name, Color colour, int numberOfSeeds) {
    super(maxAge, matureAge, spreadRate, size, name, colour);
    this.numberOfSeeds = numberOfSeeds;
  }

  public int getNumberOfSeeds() { return numberOfSeeds; }
}
