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
  private int width;
  private int height;

  private HashMap<String, List<Prey>> preys;
  private HashMap<String, List<Predator>> predators;
  private HashMap<String, List<Plant>> plants;

  private AnimalData[] preysInterval;
  private AnimalData[] predatorsInterval;
  private PlantData[] plantsInterval;

  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    preys = new HashMap<>();
    predators = new HashMap<>();
    plants = new HashMap<>();

    // The file paths are hardcoded for now
    preysInterval = Parser.parseAnimalJson(Parser.getContentsOfFile("/workspaces/junit-testing/prey_data.json"));
    predatorsInterval = Parser.parseAnimalJson(Parser.getContentsOfFile("/workspaces/junit-testing/predator_data.json"));
    plantsInterval = Parser.parsePlantJson(Parser.getContentsOfFile("/workspaces/junit-testing/plant_data.json"));

    for (int i = 0; i < preysInterval.length; i++) {
      ArrayList<Prey> preysList = new ArrayList<>();
      for (int j = 0; j < preysInterval[i].numberOfEntitiesAtStart; j++) {
        preysList.add(new Prey(preysInterval[i].name, preysInterval[i].generateRandomGenetics(), getRandomLocation(), preysInterval[i].eats));
      }
      preys.put(preysInterval[i].name, preysList);
    }

    for (int i = 0; i < predatorsInterval.length; i++) {
      ArrayList<Predator> predatorsList = new ArrayList<>();
      for (int j = 0; j < predatorsInterval[i].numberOfEntitiesAtStart; j++) {
        predatorsList.add(new Predator(predatorsInterval[i].name, predatorsInterval[i].generateRandomGenetics(), getRandomLocation(), predatorsInterval[i].eats));
      }
      predators.put(predatorsInterval[i].name, predatorsList);
    }

    for (int i = 0; i < plantsInterval.length; i++) {
      ArrayList<Plant> plantsList = new ArrayList<>();
      for (int j = 0; j < plantsInterval[i].numberOfEntitiesAtStart; j++) {
        plantsList.add(new Plant(plantsInterval[i].name, plantsInterval[i].generateRandomGenetics(), getRandomLocation()));
      }
      plants.put(plantsInterval[i].name, plantsList);
    }
  }

  private Location getRandomLocation() {
    return new Location(Math.random() * width, Math.random() * height);
  }
}