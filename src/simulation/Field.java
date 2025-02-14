package simulation;

import java.util.ArrayList;

import util.Vector;
import entities.generic.Entity;

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
  private final ArrayList<Entity> entities; // List of all entities in the field
  private final ArrayList<Entity> entitiesToSpawn = new ArrayList<>(); // Buffer list for entities to spawn

  /**
   * Constructor that is used with the JUnit tests.
   */
  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    entities = new ArrayList<>();
  }

  /**
   * Constructor with the FieldBuilder that is used in the actual simulation.
   * @param fieldBuilder The field builder to create the field.
   */
  public Field(FieldBuilder fieldBuilder) {
    this.width = fieldBuilder.getWidth();
    this.height = fieldBuilder.getHeight();
    entities = fieldBuilder.getEntities();
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
  * Filter out the entities that are not alive.
  */
  public void removeDeadEntities() {
    entities.removeIf(e -> !e.isAlive());
  }

  /**
   * @return All entities currently in the field.
   */
  public ArrayList<Entity> getEntities() {
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
}