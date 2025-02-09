package simulation;

import entities.*;
import entities.generic.Entity;
import simulation.simulationData.AnimalData;
import simulation.simulationData.Data;
import simulation.simulationData.PlantData;
import util.Vector;

import java.util.ArrayList;

/**
 * Creates a Field based off of the given simulation Data and field parameters
 * Spawns in all entities with random initial stats
 */
public class FieldBuilder {
  private final int width;
  private final int height;

  private ArrayList<Entity> entities;

  public FieldBuilder(int width, int height, Data data) {
    this.width = width;
    this.height = height;
    createEntities(data);
  }

  /**
   * @return A random position within the field as a Vector.
   */
  private Vector getRandomPosition() {
    return new Vector(Math.random() * width, Math.random() * height);
  }

  /**
   * Spawns in all preys, predators and plants for the simulation
   */
  private void createEntities(Data data) {
    entities = new ArrayList<>();

    AnimalData[] preysData = data.getPreysData();
    AnimalData[] predatorsData = data.getPredatorsData();
    PlantData[] plantsData = data.getPlantsData();

    for (AnimalData predatorData : predatorsData) {
      for (int i = 0; i < predatorData.numberOfEntitiesAtStart; i++) {
        Predator predator = new Predator(predatorData.generateRandomGenetics(), getRandomPosition());
        predator.setAge(Math.random() * predatorData.matureAge[1]);
        entities.add(predator);
      }
    }

    for (AnimalData preyData : preysData) {
      for (int i = 0; i < preyData.numberOfEntitiesAtStart; i++) {
        Prey prey = new Prey(preyData.generateRandomGenetics(), getRandomPosition());
        prey.setAge(Math.random() * preyData.matureAge[1]);
        entities.add(prey);
      }
    }

    for (PlantData plantData : plantsData) {
      for (int i = 0; i < plantData.numberOfEntitiesAtStart; i++) {
        Plant plant = new Plant(plantData.generateRandomGenetics(), getRandomPosition());
        plant.setAge(Math.random() * plantData.matureAge[1]);
        entities.add(plant);
      }
    }
  }

  public Field build() { return new Field(this); }
  public ArrayList<Entity> getEntities() { return entities; }
  public int getWidth() { return width; }
  public int getHeight() { return height; }
}