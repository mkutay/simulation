package simulation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import entities.*;
import simulation.simulationData.*;

public class Field {
  private final int width; // Width of the field
  private final int height; // Height of the field

  private final HashMap<String, List<Entity>> preys; // A map of prey species to a list of prey entities
  private final HashMap<String, List<Entity>> predators; // A map of predator species to a list of predator entities
  private final HashMap<String, List<Entity>> plants; // A map of plant species to a list of plant entities

  public Field(int width, int height) {
    this.width = width;
    this.height = height;

    //** TODO: Make hash maps just array lists - no need to keep as hash maps!
    // The hash map stores the name of the predator and then a list of all predators of that name - This is
    // unnecessary because the predator class itself stores its name! We can just keep this as an array list
    // and remove the hash map-to-array methods, and instead just check that the names match for our purposes.

    preys = new HashMap<>();
    predators = new HashMap<>();
    plants = new HashMap<>();

    // TODO Everything below should be in separate method(s) (or better yet - should all be done in a builder class)

    // Data of the simulation, given by the "researcher"
    Data givenData = new Data();

    AnimalData[] preysData = givenData.getPreysData();
    AnimalData[] predatorsData = givenData.getPredatorsData();
    PlantData[] plantsData = givenData.getPlantsData();

    HashMap<String, HashSet<String>> scaredOf = new HashMap<>(); // A map of species to a list of species that they are scared of

    // Initialise the field with randomly generated predators, according to the given data.
    for (AnimalData predatorData : predatorsData) {
        // Add the species that the predator is scared of to the scaredOf map.
        for (String scared : predatorData.eats) {
            if (!scaredOf.containsKey(scared)) scaredOf.put(scared, new HashSet<>());
            scaredOf.get(scared).add(predatorData.name);
        }

        ArrayList<Entity> predatorsList = new ArrayList<>();

        for (int j = 0; j < predatorData.numberOfEntitiesAtStart; j++) {
            predatorsList.add(new Predator(predatorData.name, predatorData.generateRandomGenetics(), getRandomPosition(), predatorData.eats));
        }

        predators.put(predatorData.name, predatorsList);
      }

    // Initialise the field with randomly generated preys, according to the given data.
    for (AnimalData preyData : preysData) {
        ArrayList<Entity> preysList = new ArrayList<>();
        String[] scaredOfArray;

        if (scaredOf.containsKey(preyData.name)) {
            scaredOfArray = new String[scaredOf.get(preyData.name).size()];
            scaredOf.get(preyData.name).toArray(scaredOfArray);
        } else {
            scaredOfArray = new String[0];
        }

        for (int j = 0; j < preyData.numberOfEntitiesAtStart; j++) {
            preysList.add(new Prey(preyData.name, preyData.generateRandomGenetics(), getRandomPosition(), preyData.eats, scaredOfArray));
        }

        preys.put(preyData.name, preysList);
    }

    // Initialise the field with randomly generated plants, according to the given data.
    for (PlantData plantData : plantsData) {
        ArrayList<Entity> plantsList = new ArrayList<>();
        for (int j = 0; j < plantData.numberOfEntitiesAtStart; j++) {
            plantsList.add(new Plant(plantData.name, plantData.generateRandomGenetics(), getRandomPosition()));
        }
        plants.put(plantData.name, plantsList);
    }
  }

  /**
   * Put an entity in-bounds if it is out of bounds (that is the field width and height).
   * @param entity The entity that will be moved.
   */
  public void putInBounds(Entity entity) {
    Vector entityPos = entity.getPosition();
    if (entityPos.x < 0) entityPos.x = 0;
    if (entityPos.y < 0) entityPos.y = 0;
    if (entityPos.x >= width) entityPos.x = width;
    if (entityPos.y >= height) entityPos.y = height;
  }

  /**
   * TODO: may not be necessary
   * @return Whether the field is viable or not, for to check if the simulation should continue.
   */
  public boolean isViable() {
    return preys.values().stream().anyMatch(list -> !list.isEmpty())
      && predators.values().stream().anyMatch(list -> !list.isEmpty());
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

  //** TODO: Move seeing methods to animal entity class
  //Seeing is a property of an animal, so it shouldn't be handled in the field.
  //Every seeing method in field should be removed and placed into animal

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
   * @param ent The entity that will be referenced as.
   * @return A list of entities that the given entity can see and is the same species. Used for reproduction.
   */
  public List<Entity> seeingSameSpecies(Entity ent) {
      switch (ent) {
          case Prey _ -> {return seeing(ent, preys.get(ent.getName()));}
          case Predator _ -> {return seeing(ent, predators.get(ent.getName()));}
          case Plant _ -> {return seeing(ent, plants.get(ent.getName()));}
          case null, default -> {
              throw new RuntimeException("Entity is not a valid type. SHOULD NOT HAPPEN.");
              //TODO remove this exception because the error shouldn't occur :)
          }
      }
  }

  /**
   * Filter out the entities that are not alive.
   */
  public void removeDeadEntities() {
    plants.values().forEach(list -> list.removeIf(entity -> !entity.isAlive()));
    preys.values().forEach(list -> list.removeIf(entity -> !entity.isAlive()));
    predators.values().forEach(list -> list.removeIf(entity -> !entity.isAlive()));
  }
  
  /**
   * @return A random position within the field.
   */
  private Vector getRandomPosition() {
    return new Vector(Math.random() * width, Math.random() * height);
  }

  /**
   * @param entity The entity that will be referenced as.
   * @param entities A map of entities to be searched through.
   * @return A list of entities that the given entity can see, searching the given list.
   */
  private List<Entity> seeing(Entity entity, List<Entity> entities) {
    List<Entity> returnEntities = new ArrayList<>();

    if (entities == null) return returnEntities;

    for (Entity ent : entities) {
      if ((ent.getPosition().subtract(entity.getPosition())).getMagnitude() < entity.getSight() + ent.getSize()) {
        returnEntities.add(ent);
      }
    }
    return returnEntities;
  }
}