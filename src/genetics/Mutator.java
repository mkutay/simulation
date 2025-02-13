package genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simulation.simulationData.*;
import util.Utility;

/**
 * Responsible for mutating genetics.
 */
public class Mutator {
  /**
   * Mutate the given plant genetics.
   * @implNote Creates a new object, instead of mutating the given one.
   * @param genetics The genetics to mutate.
   * @return The mutated genetics.
   */
  public static PlantGenetics mutatePlantGenetics(PlantGenetics genetics) {
    PlantData plantData = Arrays
      .asList(Data.getPlantsData())
      .stream()
      .filter(pd -> pd.name.equals(genetics.getName()))
      .findFirst()
      .orElse(null);

    double mutationRate = genetics.getMutationRate();
    double mutationFactor = Data.getMutationFactor();

    return new PlantGenetics(
      singleMutate(genetics.getMaxAge(), plantData.maxAge, mutationRate, mutationFactor),
      singleMutate(genetics.getMatureAge(), plantData.matureAge, mutationRate, mutationFactor),
      singleMutate(genetics.getMultiplyingRate(), plantData.multiplyingRate, mutationRate, mutationFactor),
      singleMutate(genetics.getSize(), plantData.size, mutationRate, mutationFactor),
      genetics.getName(),
      Utility.mutateColor(genetics.getColour(), mutationRate, mutationFactor),
      singleMutate(genetics.getNumberOfSeeds(), plantData.numberOfSeeds, mutationRate, mutationFactor),
      singleMutate(genetics.getMaxOffspringSpawnDistance(), plantData.maxOffspringSpawnDistance, mutationRate, mutationFactor),
      singleMutate(genetics.getOvercrowdingThreshold(), plantData.overcrowdingThreshold, mutationRate, mutationFactor),
      singleMutate(genetics.getOvercrowdingRadius(), plantData.overcrowdingRadius, mutationRate, mutationFactor),
      singleMutate(genetics.getMutationRate(), plantData.mutationRate, mutationRate, mutationFactor)
    );
  }

  public static AnimalGenetics mutateAnimalGenetics(AnimalGenetics genetics) {
    List<AnimalData> animalDatas = new ArrayList<>();
    for (AnimalData ad : Data.getPredatorsData()) {
      animalDatas.add(ad);
    }
    for (AnimalData ad : Data.getPreysData()) {
      animalDatas.add(ad);
    }
    AnimalData animalData = animalDatas
      .stream()
      .filter(ad -> ad.name.equals(genetics.getName()))
      .findFirst()
      .orElse(null);

    double mutationRate = genetics.getMutationRate();
    double mutationFactor = Data.getMutationFactor();

    return new AnimalGenetics(
      singleMutate(genetics.getMultiplyingRate(), animalData.multiplyingRate, mutationRate, mutationFactor),
      singleMutate(genetics.getMaxLitterSize(), animalData.maxLitterSize, mutationRate, mutationFactor),
      singleMutate(genetics.getMaxAge(), animalData.maxAge, mutationRate, mutationFactor),
      singleMutate(genetics.getMatureAge(), animalData.matureAge, mutationRate, mutationFactor),
      singleMutate(genetics.getMutationRate(), animalData.mutationRate, mutationRate, mutationFactor),
      singleMutate(genetics.getMaxSpeed(), animalData.maxSpeed, mutationRate, mutationFactor),
      singleMutate(genetics.getSight(), animalData.sight, mutationRate, mutationFactor),
      genetics.getGender(),
      singleMutate(genetics.getSize(), animalData.size, mutationRate, mutationFactor),
      genetics.getEats(),
      genetics.getName(),
      Utility.mutateColor(genetics.getColour(), mutationRate, mutationFactor),
      singleMutate(genetics.getOvercrowdingThreshold(), animalData.overcrowdingThreshold, mutationRate, mutationFactor),
      singleMutate(genetics.getOvercrowdingRadius(), animalData.overcrowdingRadius, mutationRate, mutationFactor),
      singleMutate(genetics.getMaxOffspringSpawnDistance(), animalData.maxOffspringSpawnDistance, mutationRate, mutationFactor)
    );
  }

  /**
   * Mutate a single value.
   * @param value The value to mutate.
   * @param interval The interval of the value (0th index is the minimum, 1st index is the maximum).
   * @param mutationRate The probability of mutation happening.
   * @param mutationFactor By how much the value can change in either direction.
   * @return The mutated value.
   */
  private static double singleMutate(double value, double[] interval, double mutationRate, double mutationFactor) {
    if (Math.random() >= mutationRate) return value;
    double mutatedValue = value + value * mutationFactor * (Math.random() > 0.5 ? 1 : -1); // Randomly increase or decrease the value.
    return Math.max(interval[0], Math.min(interval[1], mutatedValue));
  }

  private static int singleMutate(int value, int[] interval, double mutationRate, double mutationFactor) {
    if (Math.random() >= mutationRate) return value;
    int mutatedValue = (int) Math.round(value + value * mutationFactor * (Math.random() > 0.5 ? 1 : -1));
    return Math.max(interval[0], Math.min(interval[1], mutatedValue));
  }
}