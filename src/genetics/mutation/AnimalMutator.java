package genetics.mutation;

import genetics.AnimalGenetics;
import simulation.simulationData.AnimalData;
import simulation.simulationData.Data;
import util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Responsible for mutating animal genetics.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class AnimalMutator extends Mutator {
  /**
   * Mutate the given animal genetics.
   * @implNote Creates a new object, instead of mutating the given one.
   * @param genetics The genetics to mutate.
   * @return The mutated genetics.
   */
  public static AnimalGenetics mutateAnimalGenetics(AnimalGenetics genetics) {
    List<AnimalData> animalDatas = new ArrayList<>(Arrays.asList(Data.getPredatorsData()));
    animalDatas.addAll(Arrays.asList(Data.getPreysData()));
    AnimalData animalData = animalDatas
      .stream()
      .filter(ad -> ad.name.equals(genetics.getName()))
      .findFirst()
      .orElse(null);

    if (animalData == null) return null;

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
}
