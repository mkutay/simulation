package simulation;

import java.util.ArrayList;

import entities.generic.Entity;

/**
 * Holds all the simulation information and is used to step through the simulation.
 * Used in Engine. Contains the field of the entities.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Simulator {
  private int step = 0; // The current step of the simulation.
  private final Field field; // The field of the simulation.
  
  /*
  * Delta time controls simulation speed, affects things like speed of
  * entities, rate of aging, food consumption etc.
  * Not very accurate, just used to fine tune the simulation feel.
  */
  private final static double FIXED_DELTA_TIME = 0.2;

  /**
   * Constructor for the simulator.
   * @param width The width of the field.
   * @param height The height of the field.
   */
  public Simulator(int width, int height) {
    field = new FieldBuilder(width, height).build();
  }

  /**
   * @return All entities currently alive in the field.
   */
  public ArrayList<Entity> getEntities() {
    return field.getEntities();
  }

  /**
   * Simulate a single step.
   */
  public void step() {
    step++;

    ArrayList<Entity> entities = getEntities();
    
    for (Entity e : entities) {
      e.update(field, FIXED_DELTA_TIME);
      field.putInBounds(e, e.getSize());
    }

    field.spawnNewEntities();
    field.removeDeadEntities();
  }
}
