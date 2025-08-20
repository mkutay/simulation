package simulation;

import java.util.ArrayList;
import java.util.List;

import simulation.environment.Environment;
import simulation.simulationData.Data;
import entities.generic.Entity;
import simulation.quadTree.*;
import util.Vector;

/**
 * The field class is used to store all entities in the simulation.
 * It is also used to spawn entities at the end of each simulation step.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Field {
  private final int width; // Width of the field
  private final int height; // Height of the field
  private final List<Entity> entities; // List of all entities in the field
  private final List<Entity> entitiesToSpawn = new ArrayList<>(); // Buffer list for entities to spawn

  private final QuadTree quadtree; // Quadtree for optimising entity search
  private final int quadtreeCapacity = 2; // How many entities each quadtree can store before dividing

  public final Environment environment; // The environment of the field

  /**
   * Constructor that is used with the JUnit tests.
   */
  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    environment = new Environment();
    entities = new ArrayList<>();
    quadtree = new QuadTree(new Rectangle(0, 0, width, height), quadtreeCapacity);
  }

  /**
   * Constructor with the FieldBuilder that is used in the actual simulation.
   * @param fieldBuilder The field builder to create the field.
   */
  public Field(FieldBuilder fieldBuilder) {
    this.width = fieldBuilder.getWidth();
    this.height = fieldBuilder.getHeight();
    environment = new Environment();
    entities = fieldBuilder.getEntities();
    quadtree = new QuadTree(new Rectangle(0, 0, width, height), quadtreeCapacity);
  }

  /**
  * Put an entity in-bounds if it is out of bounds (that is the field width and height).
  * @param entity The entity that will be moved.
  * @param padding Padding for the size of the entity.
  */
  public void putInBounds(Entity entity, double padding) {
    Vector entityPos = entity.getPosition();
    Vector newVector = null;
    if (entityPos.x() < padding) newVector = new Vector(padding, entityPos.y());
    if (entityPos.y() < padding) newVector = new Vector(entityPos.x(), padding);
    if (entityPos.x() > (double) width - padding) newVector = new Vector((double) width - padding, entityPos.y());
    if (entityPos.y() > (double) height - padding) newVector = new Vector(entityPos.x(), (double) height - padding);
    entity.setPosition(newVector);
  }

  /**
   * @param pos A coordinate vector.
   * @param padding Internal padding for the border.
   * @return True if the coordinate is in the field (with the padding), false otherwise.
   */
  public boolean isOutOfBounds(Vector pos, double padding) {
    return (pos.x() <= padding)
      || (pos.y() <= padding)
      || (pos.x() >= (double) width - padding)
      || (pos.y() >= (double) height - padding);
  }

  /**
   * Updates the quadtree with the current entities in the field.
   */
  public void updateQuadtree() {
    quadtree.clear(); // Reset the root quadtree.
    for (Entity e : entities) {
      quadtree.insert(e);
    }
  }

  /**
   * @return The quadtree of the field.
   */
  public QuadTree getQuadtree() {
    return quadtree;
  }

  /**
   * Gets all the entities in a specified radius from a position in the field by querying the quadtree
   * @param position The center coordinates of the query range.
   * @param queryRadius The radius of the query range.
   * @return A list of entities in the field in the specified query range.
   */
  public List<Entity> queryQuadtree(Vector position, double queryRadius) {
    Circle queryRange = new Circle(position, queryRadius);
    return quadtree.query(queryRange);
  }

  /**
  * Filter out the entities that are not alive.
  */
  public void removeDeadEntities() {
    entities.removeIf(e -> !e.isAlive());
  }

  /**
   * @return All entities currently in the field.
   */
  public List<Entity> getAllEntities() {
    return entities;
  }

  /**
   * @return The width and height of the field as a Vector.
   */
  public Vector getSize() {
    return new Vector(width, height);
  }

  /**
   * Adds an entity to the field.
   * @implNote Adds to a buffer list, which is used to spawn entities in the
   * field at the end of each simulation step. Prevents concurrency errors.
   * @param entity The entity to add.
   */
  public void addEntity(Entity entity) {
    entitiesToSpawn.add(entity);
  }

  /**
   * Adds all entities in entitiesToSpawn to the entities list.
   * Used to safely add entities to the simulation.
   */
  public void spawnNewEntities() {
    entities.addAll(entitiesToSpawn);
    entitiesToSpawn.clear();
  }

  /**
   * Updates the environment of the field; that is, updates the time and weather.
   */
  public void updateEnvironment() {
    if (Data.getDoDayNightCycle()) {
      environment.updateTime(Data.getDayNightCycleSpeed() * 0.01);
    }

    if (Data.getDoWeatherCycle()) {
      environment.updateWeather();
    }
  }
}