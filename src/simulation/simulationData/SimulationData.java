package simulation.simulationData;

public class SimulationData {
  public AnimalData[] preysData; // An array of prey species data
  public AnimalData[] predatorsData; // An array of predator species data
  public PlantData[] plantsData; // An array of plant types data

  public double foodValueForAnimals; // Scales the food value of animals
  public double foodValueForPlants; // Scales the food value of plants
  public double animalHungerDrain; // Controls rate of foodLevel depletion over time
  public double animalBreedingCost; // Scales how much food is consumed on breeding; use 0 for no food cost

  public double mutationFactor; // The ratio that the genetics will mutate by
  public double entityAgeRate; // Controls how fast the entities age
  public double breedingRadiusFactorToSight; // Factor to sight that creates the radius for breeding
}
