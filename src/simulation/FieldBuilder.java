package simulation;

import entities.*;
import simulation.simulationData.AnimalData;
import simulation.simulationData.Data;
import simulation.simulationData.PlantData;

import java.util.ArrayList;

/**
 * Creates a Field based off of the given simulation Data and field parameters
 */
public class FieldBuilder {
  private final int width;
  private final int height;

  private ArrayList<Prey> preys; // A map of prey species to a list of prey entities
  private ArrayList<Predator> predators; // A map of predator species to a list of predator entities
  private ArrayList<Plant> plants; // A map of plant species to a list of plant entities

  public FieldBuilder(int width, int height, Data data) {
    this.width = width;
    this.height = height;
    createEntities(data);
  }

  private Vector getRandomPosition() {
    return new Vector(Math.random() * width, Math.random() * height);
  }

  private void createEntities(Data data) {
    preys = new ArrayList<>();
    predators = new ArrayList<>();
    plants = new ArrayList<>();

    AnimalData[] preysData = data.getPreysData();
    AnimalData[] predatorsData = data.getPredatorsData();
    PlantData[] plantsData = data.getPlantsData();

    for (AnimalData predatorData : predatorsData){
      for (int i = 0; i < predatorData.numberOfEntitiesAtStart; i++) {
        predators.add(new Predator(predatorData.generateRandomGenetics(), getRandomPosition()));
      }
    }

    for (AnimalData preyData : preysData){
      for (int i = 0; i < preyData.numberOfEntitiesAtStart; i++) {
        preys.add(new Prey(preyData.generateRandomGenetics(), getRandomPosition()));
      }
    }

    for (PlantData plantData : plantsData){
      for (int i = 0; i < plantData.numberOfEntitiesAtStart; i++) {
        plants.add(new Plant(plantData.generateRandomGenetics(), getRandomPosition()));
      }
    }
  }

  public ArrayList<Prey> getPreys() { return preys; }
  public ArrayList<Predator> getPredators() { return predators; }
  public ArrayList<Plant> getPlants() { return plants; }
  public int getWidth() { return width; }
  public int getHeight() { return height; }

  public Field build(){
    return new Field(this);
  }
}

