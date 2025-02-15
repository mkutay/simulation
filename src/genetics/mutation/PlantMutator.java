package genetics.mutation;

import genetics.PlantGenetics;
import simulation.simulationData.Data;
import simulation.simulationData.PlantData;
import util.Utility;

import java.util.Arrays;

/**
 * Mutator for plant genetics.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class PlantMutator extends Mutator {
  /**
   * Mutate the given plant genetics.
   * @implNote Creates a new object, instead of mutating the given one.
   * @param genetics The genetics to mutate.
   * @return The mutated genetics.
   */
  public static PlantGenetics mutatePlantGenetics(PlantGenetics genetics) {
    PlantData plantData = Arrays.stream(Data.getPlantsData())
      .filter(pd -> pd.name.equals(genetics.getName()))
      .findFirst()
      .orElse(null);

    if (plantData == null) return null;
    
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
}
