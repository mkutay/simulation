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

  private final AnimalData[] preysData; // An array of prey species data
  private final AnimalData[] predatorsData; // An array of predator species data
  private final PlantData[] plantsData; // An array of plant types data

  /**
   * Constructor -- Parse and create all the data from the JSON files.
   */
  public Data() {
    preysData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/prey_data.json"));
    predatorsData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/predator_data.json"));
    plantsData = Parser.parsePlantJson(Parser.getContentsOfFile(PATH + "/plant_data.json"));
  }

  // Getters:
  public AnimalData[] getPreysData() { return preysData; }
  public AnimalData[] getPredatorsData() { return predatorsData; }
  public PlantData[] getPlantsData() { return plantsData; }
}
