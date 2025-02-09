package genetics;

import java.awt.Color;

public abstract class Genetics {
  private final String name; // Name of entity, acts as identifying key
  private final int maxAge; // Maximum age of the entity
  private final int matureAge; // Age at which the entity can start breeding
  private final double multiplyingRate; // Rate at which the entity multiplies -- breads (animal) or spreads (plant)
  private final int size; // Size of the entity (the radius of the circle representing the entity)
  private final Color colour; // RGB colour of the entity
  private final int overcrowdingThreshold; // Number of entities at which the entity will die
  private final double overcrowdingRadius; // Radius inside of which the threshold is checked
  private final double maxOffspringSpawnDistance; // Maximum distance from the parent entity that the offspring can spawn

  public Genetics(int maxAge, int matureAge, double multiplyingRate, int size, String name, Color colour, int overcrowdingThreshold, double overcrowdingRadius, double maxOffspringSpawnDistance) {
    this.maxAge = maxAge;
    this.matureAge = matureAge;
    this.multiplyingRate = multiplyingRate;
    this.size = size;
    this.name = name;
    this.colour = colour;
    this.overcrowdingThreshold = overcrowdingThreshold;
    this.overcrowdingRadius = overcrowdingRadius;
    this.maxOffspringSpawnDistance = maxOffspringSpawnDistance;
  }

  // Getters:
  public String getName() { return name; }
  public int getMaxAge() { return maxAge; }
  public int getMatureAge() { return matureAge; }
  public double getMultiplyingRate() { return multiplyingRate; }
  public int getSize() { return size; }
  public Color getColour() { return colour; }
  public int getOvercrowdingThreshold() { return overcrowdingThreshold; }
  public double getOvercrowdingRadius() { return overcrowdingRadius; }
  public double getMaxOffspringSpawnDistance() { return maxOffspringSpawnDistance; }
}