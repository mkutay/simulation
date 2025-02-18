package simulation;

import entities.*;
import simulation.simulationData.*;
import entities.generic.Entity;
import util.Vector;

import java.util.ArrayList;

/**
 * Creates a Field based off of the given simulation Data and field parameters.
 * Spawns in all entities with random initial stats.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class FieldBuilder {
  private final int width; // The width of the field.
  private final int height; // The height of the field.
  private ArrayList<Entity> entities; // The list of entities.

  /**
   * Constructor.
   * @param width The width of the field.
   * @param height The height of the field.
   * @param data The simulation data.
   */
  public FieldBuilder(int width, int height) {
    this.width = width;
    this.height = height;
    createEntities();
  }

  /**
   * @return A random position within the field as a Vector.
   */
  private Vector getRandomPosition() {
    return new Vector(Math.random() * width, Math.random() * height);
  }

  /**
   * Create all preys, predators, and plants for the simulation.
   */
  private void createEntities() {
    entities = new ArrayList<>();

    AnimalData[] preysData = Data.getPreysData();
    AnimalData[] predatorsData = Data.getPredatorsData();
    PlantData[] plantsData = Data.getPlantsData();

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

  // Getters:
  public Field build() { return new Field(this); }
  public ArrayList<Entity> getEntities() { return entities; }
  public int getWidth() { return width; }
  public int getHeight() { return height; }
}