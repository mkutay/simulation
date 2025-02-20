package genetics;

import java.awt.Color;

/**
 * An abstract genetics class for entities.
 * Contains genetic values that are both common to animals and plants.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public abstract class Genetics {
  private final String name; // Name of entity, acts as identifying key.
  private final int maxAge; // Maximum age of the entity.
  private final int matureAge; // Age at which the entity can start breeding.
  private final double multiplyingRate; // Rate at which the entity multiplies -- breads (animal) or spreads (plant).
  private final int size; // Size of the entity (the radius of the circle representing the entity).
  private final Color colour; // RGB colour of the entity.
  private final int overcrowdingThreshold; // Number of entities at which the entity will die.
  private final double overcrowdingRadius; // Radius inside of which the threshold is checked.
  private final double maxOffspringSpawnDistance; // Maximum distance from the parent entity that the offspring can spawn.
  private final double mutationRate; // Rate at which the entity's genetics can mutate.

  /**
   * Constructor.
   */
  public Genetics(int maxAge, int matureAge, double multiplyingRate, int size, String name, Color colour, int overcrowdingThreshold, double overcrowdingRadius, double maxOffspringSpawnDistance, double mutationRate) {
    this.maxAge = maxAge;
    this.matureAge = matureAge;
    this.multiplyingRate = multiplyingRate;
    this.size = size;
    this.name = name;
    this.colour = colour;
    this.overcrowdingThreshold = overcrowdingThreshold;
    this.overcrowdingRadius = overcrowdingRadius;
    this.maxOffspringSpawnDistance = maxOffspringSpawnDistance;
    this.mutationRate = mutationRate;
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
  public double getMutationRate() { return mutationRate; }
}