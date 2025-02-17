package simulation;

import java.util.ArrayList;
import java.util.HashMap;

import entities.generic.Entity;
import simulation.simulationData.Data;

/**
 * Holds all the simulation information and is used to step through the simulation.
 * Used in Engine. Contains the field of the entities.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Simulator {
  private final Field field; // The field of the simulation.

  /**
  * Delta time controls simulation speed, affects things like speed of
  * entities, rate of aging, food consumption etc.
  * Not very accurate, just used to fine tune the simulation feel.
  */
  private final static double FIXED_DELTA_TIME = 0.2;

  /**
   * How much to affect the day/night cycle per frame. Independent of delta time.
   */
  private final static double DAY_NIGHT_CYCLE_RATE = 0.001;


  /**
   * Constructor for the simulator.
   * @param width The width of the field.
   * @param height The height of the field.
   */
  public Simulator(int width, int height) {
    field = new FieldBuilder(width, height).build();
  }

  /**
   * Simulate a single step.
   */
  public void step() {
    ArrayList<Entity> entities = field.getAllEntities();
    
    for (Entity e : entities) {
      e.update(field, FIXED_DELTA_TIME);
      field.putInBounds(e, e.getSize());
    }

    field.spawnNewEntities();
    field.removeDeadEntities();
    field.updateQuadtree();

    if (Data.getDoDayNightCycle()){
      field.environment.incrementTime(DAY_NIGHT_CYCLE_RATE);
    }

    if (Data.getDoWeatherCycle()){
      field.environment.updateWeather();
    }

  }

  /**
   * Get the field of the simulator.
   * @return The field of the simulator.
   */
  public Field getField() {
    return field;
  }

  /**
   * @return A hash map of entity names to the number of existing entities in the field
   */
  public HashMap<String, Integer> getFieldData(){
    HashMap<String, Integer> fieldData = new HashMap<>();
    for (Entity e : field.getAllEntities()) { //Count all the entities in the field
      fieldData.put(e.getName(), fieldData.getOrDefault(e.getName(), 0) + 1);
    }

    return fieldData;
  }
}
