package simulation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import entities.Location;
import entities.Plant;
import entities.Entity;
import entities.Animal;
import entities.Predator;
import entities.Prey;
import genetics.AnimalData;
import genetics.PlantData;

public class Field {
  private int width; // Width of the field
  private int height; // Height of the field

  private Data givenData; // Data of the simulation, given by the "researcher"

  private HashMap<String, List<Entity>> preys; // A map of prey species to a list of prey entities
  private HashMap<String, List<Entity>> predators; // A map of predator species to a list of predator entities
  private HashMap<String, List<Entity>> plants; // A map of plant species to a list of plant entities

  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    preys = new HashMap<>();
    predators = new HashMap<>();
    plants = new HashMap<>();
    givenData = new Data();

    AnimalData[] preysData = givenData.getPreysData();
    AnimalData[] predatorsData = givenData.getPredatorsData();
    PlantData[] plantsData = givenData.getPlantsData();

    HashMap<String, HashSet<String>> scaredOf = new HashMap<>(); // A map of species to a list of species that they are scared of

    // Initialise the field with randomly generated predators, according to the given data.
    for (int i = 0; i < predatorsData.length; i++) {
      // Add the species that the predator is scared of to the scaredOf map.
      for (String scared : predatorsData[i].eats) {
        if (!scaredOf.containsKey(scared)) scaredOf.put(scared, new HashSet<>());
        scaredOf.get(scared).add(predatorsData[i].name);
      }

      ArrayList<Entity> predatorsList = new ArrayList<>();
      
      for (int j = 0; j < predatorsData[i].numberOfEntitiesAtStart; j++) {
        predatorsList.add(new Predator(predatorsData[i].name, predatorsData[i].generateRandomGenetics(), getRandomLocation(), predatorsData[i].eats));
      }

      predators.put(predatorsData[i].name, predatorsList);
    }

    // Initialise the field with randomly generated preys, according to the given data.
    for (int i = 0; i < preysData.length; i++) {
      ArrayList<Entity> preysList = new ArrayList<>();
      String[] scaredOfArray;

      if (scaredOf.containsKey(preysData[i].name)) {
        scaredOfArray = new String[scaredOf.get(preysData[i].name).size()];
        scaredOf.get(preysData[i].name).toArray(scaredOfArray);
      } else {
        scaredOfArray = new String[0];
      }

      for (int j = 0; j < preysData[i].numberOfEntitiesAtStart; j++) {
        preysList.add(new Prey(preysData[i].name, preysData[i].generateRandomGenetics(), getRandomLocation(), preysData[i].eats, scaredOfArray));
      }

      preys.put(preysData[i].name, preysList);
    }

    // Initialise the field with randomly generated plants, according to the given data.
    for (int i = 0; i < plantsData.length; i++) {
      ArrayList<Entity> plantsList = new ArrayList<>();
      for (int j = 0; j < plantsData[i].numberOfEntitiesAtStart; j++) {
        plantsList.add(new Plant(plantsData[i].name, plantsData[i].generateRandomGenetics(), getRandomLocation()));
      }
      plants.put(plantsData[i].name, plantsList);
    }
  }

  /**
   * Put an entity in-bounds if it is out of bounds (that is the field width and height).
   * @param entity The entity that will be moved.
   */
  public void putInBounds(Entity entity) {
    Location location = entity.getLocation();
    if (location.x < 0) location.x = 0;
    if (location.y < 0) location.y = 0;
    if (location.x >= width) location.x = width;
    if (location.y >= height) location.y = height;
  }

  /**
   * @return Whether the field is viable or not, for to check if the simulation should continue.
   */
  public boolean isViable() {
    return preys.values().stream().anyMatch(list -> list.size() > 0)
      && predators.values().stream().anyMatch(list -> list.size() > 0);
  }

  /**
   * Converts the map of entities of preys to a list of preys.
   */
  public List<Prey> getPreys() {
    List<Prey> returnList = new ArrayList<>();
    preys.values().forEach(list -> list.forEach(entity -> returnList.add((Prey) entity)));
    return returnList;
  }

  /**
   * Converts the map of entities of predators to a list of predators.
   */
  public List<Predator> getPredators() {
    List<Predator> returnList = new ArrayList<>();
    predators.values().forEach(list -> list.forEach(entity -> returnList.add((Predator) entity)));
    return returnList;
  }

  /**
   * Converts the map of entities of plants to a list of plants.
   */
  public List<Plant> getPlants() {
    List<Plant> returnList = new ArrayList<>();
    plants.values().forEach(list -> list.forEach(entity -> returnList.add((Plant) entity)));
    return returnList;
  }

  /**
   * @param entity The entity that will be referenced as.
   * @return A list of preys that the given entity can see and wants to "eat".
   */
  public List<Prey> seeingPreys(Predator entity) {
    List<Prey> returnList = new ArrayList<>();
    Arrays.asList(entity.getEats()).forEach(eats -> seeing(entity, preys.get(eats)).forEach(e -> returnList.add((Prey) e)));
    return returnList;
  }

  /**
   * @param entity The entity that will be referenced as.
   * @return A list of predators that the given entity can see and wants to run away from.
   */
  public List<Predator> seeingPredators(Prey entity) {
    List<Predator> returnList = new ArrayList<>();
    Arrays.asList(entity.getScaredOf()).forEach(scaredOf -> seeing(entity, predators.get(scaredOf)).forEach(e -> returnList.add((Predator) e)));
    return returnList;
  }

  /**
   * @param entity The entity that will be referenced as.
   * @return A list of plants that the given entity can see and wants to "eat".
   */
  public List<Plant> seeingPlants(Prey entity) {
    List<Plant> returnList = new ArrayList<>();
    Arrays.asList(entity.getEats()).forEach(eats -> seeing(entity, plants.get(eats)).forEach(e -> returnList.add((Plant) e)));
    return returnList;
  }

  /**
   * @param entity The entity that will be referenced as.
   * @return A list of entities that the given entity can see and is the same species. Used for reproduction.
   */
  public List<Entity> seeingSameSpecies(Entity ent) {
    if (ent instanceof Prey) {
      return seeing(ent, preys.get(ent.getName()));
    } else if (ent instanceof Predator) {
      return seeing(ent, plants.get(ent.getName()));
    } else if (ent instanceof Plant) {
      return seeing(ent, plants.get(ent.getName()));
    } else {
      System.out.println("Entity is not a valid type. SHOULD NOT HAPPEN.");
      return null;
    }
  }

  /**
   * Filter out the entities that are not alive.
   */
  public void filterEntities() {
    plants.values().forEach(list -> list.removeIf(entity -> !entity.isAlive()));
    preys.values().forEach(list -> list.removeIf(entity -> !entity.isAlive()));
    predators.values().forEach(list -> list.removeIf(entity -> !entity.isAlive()));
  }
  
  /**
   * @return A random location within the field.
   */
  private Location getRandomLocation() {
    return new Location(Math.random() * width, Math.random() * height);
  }

  /**
   * @param entity The entity that will be referenced as.
   * @param entities A map of entities to be searched through.
   * @return A list of entities that the given entity can see, searching the given list.
   */
  private List<Entity> seeing(Entity entity, List<Entity> entities) {
    List<Entity> returnEntities = new ArrayList<>();

    if (entities == null) return returnEntities;

    entities.stream()
      .filter(ent -> ent.getLocation().distance(entity.getLocation()) < entity.getSight() + ent.getSize())
      .forEach(returnEntities::add);

    return returnEntities;
  }
}