import java.util.ArrayList;
import java.util.List;

import entities.Plant;
import entities.Predator;
import entities.Prey;
import genetics.AnimalData;
import genetics.PlantData;

public class Field {
  private int width;
  private int height;

  private List<Prey> preys;
  private List<Predator> predators;
  private List<Plant> plants;

  private AnimalData[] preysInterval;
  private AnimalData[] predatorsInterval;
  private PlantData[] plantsInterval;

  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    preys = new ArrayList<>();
    predators = new ArrayList<>();
    plants = new ArrayList<>();

    // The file paths are hardcoded for now
    preysInterval = Parser.parseAnimalJson(Parser.getContentsOfFile("/workspaces/junit-testing/prey_data.json"));
    predatorsInterval = Parser.parseAnimalJson(Parser.getContentsOfFile("/workspaces/junit-testing/predator_data.json"));
    plantsInterval = Parser.parsePlantJson(Parser.getContentsOfFile("/workspaces/junit-testing/plant_data.json"));

    for (int i = 0; i < preysInterval.length; i++) {
      for (int j = 0; j < preysInterval[i].numberOfEntitiesAtStart; j++) {
        preys.add(new Prey(0, preysInterval[i].name, preysInterval[i].generateRandomGenetics()));
      }
    }

    for (int i = 0; i < predatorsInterval.length; i++) {
      for (int j = 0; j < predatorsInterval[i].numberOfEntitiesAtStart; j++) {
        predators.add(new Predator(0, predatorsInterval[i].name, predatorsInterval[i].generateRandomGenetics()));
      }
    }

    for (int i = 0; i < plantsInterval.length; i++) {
      for (int j = 0; j < plantsInterval[i].numberOfEntitiesAtStart; j++) {
        plants.add(new Plant(0, plantsInterval[i].name, plantsInterval[i].generateRandomGenetics()));
      }
    }
  }
}
