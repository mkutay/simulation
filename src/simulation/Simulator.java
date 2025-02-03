package simulation;

import entities.Prey;
import entities.Predator;
import entities.Plant;
import entities.generic.Entity;
import simulation.simulationData.Data;

import java.util.ArrayList;

/**
 * Holds all the simulation information and is used to step through the simulation
 * Used in Engine
 */
public class Simulator {  
  private int step;
  private final Field field;

  public Simulator(int width, int height) {
    step = 0;
    Data simulationData = new Data();
    field = new FieldBuilder(width, height, simulationData).build();
  }

  /**
   * @return All entities currently alive in the field
   */
  public ArrayList<Entity> getEntities(){
    return field.getEntities();
  }

  //TODO re-implement movement and add breeding system + eating/hunger system

  /**
   * Simulates a single step of the simulation
   */
  public void step() {
    step++;

    System.out.println("Step " + step);

    for (Plant plant : field.getPlants()) {
      //plant.update();
      System.out.println(plant);
      plant.multiply();
      plant.incrementAge();
    }

    for (Prey prey : field.getPreys()) {
      System.out.println(prey);
      //prey.update();
      prey.incrementAge();
      field.putInBounds(prey);
    }

    for (Predator predator : field.getPredators()) {
      System.out.println(predator);
      //predator.update();
      predator.incrementAge();
      field.putInBounds(predator);
    }

    field.removeDeadEntities();
  }
}
