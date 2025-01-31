import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import entities.Location;
import entities.Plant;
import entities.Predator;
import entities.Prey;
import genetics.AnimalData;
import genetics.PlantData;

public class Field {
  public static final String PATH = "/Users/kutay/code/we-get-these-100s"; // Change this to the path of the project -- this is temporary

  private int width;
  private int height;

  private HashMap<String, List<Prey>> preys;
  private HashMap<String, List<Predator>> predators;
  private HashMap<String, List<Plant>> plants;

  private AnimalData[] preysData;
  private AnimalData[] predatorsData;
  private PlantData[] plantsData;

  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    preys = new HashMap<>();
    predators = new HashMap<>();
    plants = new HashMap<>();

    // The file paths are hardcoded for now
    preysData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/prey_data.json"));
    predatorsData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/predator_data.json"));
    plantsData = Parser.parsePlantJson(Parser.getContentsOfFile(PATH + "/plant_data.json"));

    for (int i = 0; i < preysData.length; i++) {
      ArrayList<Prey> preysList = new ArrayList<>();
      for (int j = 0; j < preysData[i].numberOfEntitiesAtStart; j++) {
        preysList.add(new Prey(preysData[i].name, preysData[i].generateRandomGenetics(), getRandomLocation(), preysData[i].eats));
      }
      preys.put(preysData[i].name, preysList);
    }

    for (int i = 0; i < predatorsData.length; i++) {
      ArrayList<Predator> predatorsList = new ArrayList<>();
      for (int j = 0; j < predatorsData[i].numberOfEntitiesAtStart; j++) {
        predatorsList.add(new Predator(predatorsData[i].name, predatorsData[i].generateRandomGenetics(), getRandomLocation(), predatorsData[i].eats));
      }
      predators.put(predatorsData[i].name, predatorsList);
    }

    for (int i = 0; i < plantsData.length; i++) {
      ArrayList<Plant> plantsList = new ArrayList<>();
      for (int j = 0; j < plantsData[i].numberOfEntitiesAtStart; j++) {
        plantsList.add(new Plant(plantsData[i].name, plantsData[i].generateRandomGenetics(), getRandomLocation()));
      }
      plants.put(plantsData[i].name, plantsList);
    }
  }

  private Location getRandomLocation() {
    return new Location(Math.random() * width, Math.random() * height);
  }
}