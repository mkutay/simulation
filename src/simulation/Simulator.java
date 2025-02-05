package simulation;

import java.util.ArrayList;

import entities.generic.Entity;
import simulation.simulationData.Data;

/**
 * Holds all the simulation information and is used to step through the simulation
 * Used in Engine
 */
public class Simulator {  
  private int step = 0;
  private final Field field;
  private final static double FIXED_DELTA_TIME = 0.5; // Effectively controls simulation speed

  public Simulator(int width, int height) {
    Data simulationData = new Data();
    field = new FieldBuilder(width, height, simulationData).build();
  }

  /**
   * @return All entities currently alive in the field.
   */
  public ArrayList<Entity> getEntities() {
    return field.getEntities();
  }

  // TODO re-implement movement and add breeding system + eating/hunger system

  /**
   * Simulates a single step.
   */
  public void step() {
    step++;

    // System.out.println("Step " + step);

    ArrayList<Entity> entities = getEntities();
    for (Entity e : entities) {
      e.update(field, FIXED_DELTA_TIME);
      field.putInBounds(e);
    }

    field.removeDeadEntities();
  }
}
