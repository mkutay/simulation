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

  private final SimulationData simulationData; // The data of the simulation

  /**
   * Constructor -- Parse and create all the data from the JSON file.
   */
  public Data() {
    simulationData = Parser.parseSimulationData(Parser.getContentsOfFile(PATH + "/simulation_data.json"));
  }

  // Getters:
  public AnimalData[] getPreysData() { return simulationData.preysData; }
  public AnimalData[] getPredatorsData() { return simulationData.predatorsData; }
  public PlantData[] getPlantsData() { return simulationData.plantsData; }
  public double getFoodValueForAnimals() { return simulationData.foodValueForAnimals; }
  public double getFoodValueForPlants() { return simulationData.foodValueForPlants; }
  public double getAnimalHungerDrain() { return simulationData.animalHungerDrain; }
  public double getAnimalBreedingCost() { return simulationData.animalBreedingCost; }
}
