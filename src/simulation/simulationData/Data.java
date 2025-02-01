package simulation.simulationData;

import simulation.Parser;

public class Data {
  public static final String PATH = System.getProperty("user.dir");

  private final AnimalData[] preysData; // An array of prey species data
  private final AnimalData[] predatorsData; // An array of predator species data
  private final PlantData[] plantsData; // An array of plant types data

  public Data() {
    preysData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/prey_data.json"));
    predatorsData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/predator_data.json"));
    plantsData = Parser.parsePlantJson(Parser.getContentsOfFile(PATH + "/plant_data.json"));
  }

  public AnimalData[] getPreysData() { return preysData; }
  public AnimalData[] getPredatorsData() { return predatorsData; }
  public PlantData[] getPlantsData() { return plantsData; }
}
