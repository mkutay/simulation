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
    List<AnimalData> animalDatas = new ArrayList<>();
    // Add all of the animal data to the list, without differentiating between predators and preys:
    animalDatas.addAll(Arrays.asList(Data.getPredatorsData()));
    animalDatas.addAll(Arrays.asList(Data.getPreysData()));

    // Get the specific animal data for this species:
    AnimalData animalData = animalDatas
      .stream()
      .filter(ad -> ad.name.equals(genetics.getName()))
      .findFirst()
      .orElse(null);

    if (animalData == null) return null;
    double mutationRate = genetics.getMutationRate();

    return new AnimalGenetics(
      singleMutate(genetics.getMultiplyingRate(), animalData.multiplyingRate, mutationRate),
      singleMutate(genetics.getMaxLitterSize(), animalData.maxLitterSize, mutationRate),
      singleMutate(genetics.getMaxAge(), animalData.maxAge, mutationRate),
      singleMutate(genetics.getMatureAge(), animalData.matureAge, mutationRate),
      singleMutate(genetics.getMutationRate(), animalData.mutationRate, mutationRate),
      singleMutate(genetics.getMaxSpeed(), animalData.maxSpeed, mutationRate),
      singleMutate(genetics.getSight(), animalData.sight, mutationRate),
      genetics.getGender(),
      singleMutate(genetics.getSize(), animalData.size, mutationRate),
      genetics.getEats(),
      genetics.getName(),
      Utility.mutateColor(genetics.getColour(), mutationRate),
      singleMutate(genetics.getOvercrowdingThreshold(), animalData.overcrowdingThreshold, mutationRate),
      singleMutate(genetics.getOvercrowdingRadius(), animalData.overcrowdingRadius, mutationRate),
      singleMutate(genetics.getMaxOffspringSpawnDistance(), animalData.maxOffspringSpawnDistance, mutationRate)
    );
  }
}
