package genetics;

import genetics.mutation.PlantMutator;
import java.awt.Color;

/**
 * PlantGenetics class. Contains the genetics of a plant entity.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class PlantGenetics extends Genetics {
  private final int numberOfSeeds; // Number of seeds produced by the plant, when multiplying.
  private final double rainingGrowthFactor; // The factor by which the plant grows when it is raining.

  /**
   * Constructor -- Creates a new set of genetics for a plant.
   */
  public PlantGenetics(int maxAge, int matureAge, double multiplyingRate, int size, String name, Color colour, int numberOfSeeds, double maxOffspringSpawnDistance, int overcrowdingThreshold, double overcrowdingRadius, double mutationRate, double rainingGrowthFactor) {
    super(maxAge, matureAge, multiplyingRate, size, name, colour, overcrowdingThreshold, overcrowdingRadius, maxOffspringSpawnDistance, mutationRate);
    this.numberOfSeeds = numberOfSeeds;
    this.rainingGrowthFactor = rainingGrowthFactor;
  }

  /**
   * Get the genetics of the offspring, created from this genetics.
   * Can be mutated.
   * @return The genetics of the offspring.
   */
  public PlantGenetics getOffspringGenetics() {
    return PlantMutator.mutatePlantGenetics(this);
  }

  // Getters:
  public int getNumberOfSeeds() { return numberOfSeeds; }
  public double getRainingGrowthFactor() { return rainingGrowthFactor; }
}
