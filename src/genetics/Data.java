package genetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import main.Parser;
import entities.Predator;

public class Data {
  public static final String PATH = "/Users/kutay/code/we-get-these-100s"; // Change this to the path of the project -- this is temporary

  private AnimalData[] preysData; // An array of prey species data
  private AnimalData[] predatorsData; // An array of predator species data
  private PlantData[] plantsData; // An array of plant types data

  public Data() {
    preysData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/prey_data.json"));
    predatorsData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/predator_data.json"));
    plantsData = Parser.parsePlantJson(Parser.getContentsOfFile(PATH + "/plant_data.json"));
  }

  public AnimalData[] getPreysData() { return preysData; }
  public AnimalData[] getPredatorsData() { return predatorsData; }
  public PlantData[] getPlantsData() { return plantsData; }
}
