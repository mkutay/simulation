package simulation.simulationData;

import util.Parser;

/**
 * A class to store all the data of the simulation.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Data {
  public static final String PATH = System.getProperty("user.dir"); // The main directory of the project.

  // The data of the simulation:
  private static final SimulationData simulationData = Parser.parseSimulationData(Parser.getContentsOfFile(PATH + "/src/simulation_data.json"));

  // Getters:
  public static AnimalData[] getPreysData() { return simulationData.preysData; }
  public static AnimalData[] getPredatorsData() { return simulationData.predatorsData; }
  public static PlantData[] getPlantsData() { return simulationData.plantsData; }
  public static double getFoodValueForAnimals() { return simulationData.foodValueForAnimals; }
  public static double getFoodValueForPlants() { return simulationData.foodValueForPlants; }
  public static double getAnimalHungerDrain() { return simulationData.animalHungerDrain; }
  public static double getAnimalBreedingCost() { return simulationData.animalBreedingCost; }
  public static double getMutationFactor() { return simulationData.mutationFactor; }
  public static double getEntityAgeRate() { return simulationData.entityAgeRate; }
  public static double getFieldScaleFactor() { return simulationData.fieldScaleFactor; }
  public static boolean getDoDayNightCycle() { return simulationData.doDayNightCycle; }
  public static boolean getDoWeatherCycle() { return simulationData.doWeatherCycle; }
  public static double getWeatherChangeProbability() { return simulationData.weatherChangeProbability; }
  public static double getWindStrength() { return simulationData.windStrength; }
  public static double getStormMovementSpeedFactor() { return simulationData.stormMovementSpeedFactor; }
  public static boolean getShowQuadTrees() { return simulationData.showQuadTrees; }
  public static double getDayNightCycleSpeed() { return simulationData.dayNightCycleSpeed; }
  public static double getAnimalDyingOfHungerThreshold() { return simulationData.animalDyingOfHungerThreshold; }
  public static double getAnimalHungerThreshold() { return simulationData.animalHungerThreshold; }
}
