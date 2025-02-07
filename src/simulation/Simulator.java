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
    field = new FieldBuilder(width, height, new Data()).build();
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

    System.out.println("Step " + step);

    ArrayList<Entity> entities = getEntities();
    for (int i = 0; i < entities.size(); i++) {
      Entity e = entities.get(i);
      e.update(field, FIXED_DELTA_TIME);
      field.putInBounds(e, e.getSize());
    }

    field.removeDeadEntities();
  }
}
