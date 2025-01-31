import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import entities.Location;
import entities.Plant;
import entities.Entity;
import entities.Animal;
import entities.Predator;
import entities.Prey;
import genetics.AnimalData;
import genetics.PlantData;

public class Field {
  public static final String PATH = "/Users/kutay/code/we-get-these-100s"; // Change this to the path of the project -- this is temporary

  private int width; // Width of the field
  private int height; // Height of the field

  private HashMap<String, List<Prey>> preys; // A map of prey species to a list of prey entities
  private HashMap<String, List<Predator>> predators; // A map of predator species to a list of predator entities
  private HashMap<String, List<Plant>> plants; // A map of plant species to a list of plant entities

  private AnimalData[] preysData; // An array of prey species data
  private AnimalData[] predatorsData; // An array of predator species data
  private PlantData[] plantsData; // An array of plant types data

  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    preys = new HashMap<>();
    predators = new HashMap<>();
    plants = new HashMap<>();

    preysData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/prey_data.json"));
    predatorsData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/predator_data.json"));
    plantsData = Parser.parsePlantJson(Parser.getContentsOfFile(PATH + "/plant_data.json"));

    // Initialise the field with randomly generated preys, according to the given data.
    for (int i = 0; i < preysData.length; i++) {
      ArrayList<Prey> preysList = new ArrayList<>();
      for (int j = 0; j < preysData[i].numberOfEntitiesAtStart; j++) {
        preysList.add(new Prey(preysData[i].name, preysData[i].generateRandomGenetics(), getRandomLocation(), preysData[i].eats));
      }
      preys.put(preysData[i].name, preysList);
    }

    // Initialise the field with randomly generated predators, according to the given data.
    for (int i = 0; i < predatorsData.length; i++) {
      ArrayList<Predator> predatorsList = new ArrayList<>();
      for (int j = 0; j < predatorsData[i].numberOfEntitiesAtStart; j++) {
        predatorsList.add(new Predator(predatorsData[i].name, predatorsData[i].generateRandomGenetics(), getRandomLocation(), predatorsData[i].eats));
      }
      predators.put(predatorsData[i].name, predatorsList);
    }

    // Initialise the field with randomly generated plants, according to the given data.
    for (int i = 0; i < plantsData.length; i++) {
      ArrayList<Plant> plantsList = new ArrayList<>();
      for (int j = 0; j < plantsData[i].numberOfEntitiesAtStart; j++) {
        plantsList.add(new Plant(plantsData[i].name, plantsData[i].generateRandomGenetics(), getRandomLocation()));
      }
      plants.put(plantsData[i].name, plantsList);
    }
  }

  /**
   * @return A random location within the field.
   */
  private Location getRandomLocation() {
    return new Location(Math.random() * width, Math.random() * height);
  }

  /**
   * @param ent The entity that will be referenced as.
   * @return A list of entities that the given entity can see.
   */
  private List<Entity> seeing(Animal ent) {
    List<Entity> entities = new ArrayList<>();
    for (List<Predator> preds : predators.values()) {
      for (Predator pred : preds) {
        if (ent.getLocation().distance(pred.getLocation()) < ent.getSight()) {
          entities.add(pred);
        }
      }
    }
    return entities;
  }
}