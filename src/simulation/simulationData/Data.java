package simulation.simulationData;

import util.Parser;

/**
 * A class to store all the data of the simulation.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Data {
  public static final String PATH = System.getProperty("user.dir"); // The main directory of the project

  // The data of the simulation
  private static final SimulationData simulationData = Parser.parseSimulationData(Parser.getContentsOfFile(PATH + "/simulation_data.json"));

  // Getters:
  public static AnimalData[] getPreysData() { return simulationData.preysData; }
  public static AnimalData[] getPredatorsData() { return simulationData.predatorsData; }
  public static PlantData[] getPlantsData() { return simulationData.plantsData; }
  public static double getFoodValueForAnimals() { return simulationData.foodValueForAnimals; }
  public static double getFoodValueForPlants() { return simulationData.foodValueForPlants; }
  public static double getAnimalHungerDrain() { return simulationData.animalHungerDrain; }
  public static double getAnimalBreedingCost() { return simulationData.animalBreedingCost; }
  public static double getMutationFactor() { return simulationData.mutationFactor; }
}
