package simulation;

import java.util.ArrayList;

import util.Vector;
import entities.generic.Entity;

public class Field {
  private final int width; // Width of the field
  private final int height; // Height of the field

  private final ArrayList<Entity> entities;

  /**
   * For use with the JUnit tests.
   */
  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    entities = new ArrayList<>();
  }

  public Field(FieldBuilder fieldBuilder) {
    this.width = fieldBuilder.getWidth();
    this.height = fieldBuilder.getHeight();
    entities = fieldBuilder.getEntities();
  }

  /**
  * Put an entity in-bounds if it is out of bounds (that is the field width and height).
  * @param entity The entity that will be moved.
  */
  public void putInBounds(Entity entity, double padding) {
    Vector entityPos = entity.getPosition();
    if (entityPos.x < padding) entityPos.x = padding;
    if (entityPos.y < padding) entityPos.y = padding;
    if (entityPos.x > width - padding) entityPos.x = width - padding;
    if (entityPos.y > height - padding) entityPos.y = height - padding;
  }

  /**
   * @param pos A coordinate vector.
   * @param padding Internal padding for the border.
   * @return True if the coordinate is in the field (with the padding), false otherwise.
   */
  public boolean isOutOfBounds(Vector pos, double padding) {
    return (pos.x < padding) || (pos.y < padding) || (pos.x >= width - padding) || (pos.y >= height - padding);
  }

  /**
  * Filter out the entities that are not alive.
  */
  public void removeDeadEntities() {
    entities.removeIf(e -> !e.isAlive());
  }

  /**
   * TODO: Is really needed?
   * @return The number of entities in the field
   */
  public int getTotalNumEntities() {
    return entities.size();
  }

  /**
   * @return All entities currently alive in the field.
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
   * Add an entity to the field.
   * @param entity The entity to add.
   */
  public void addEntity(Entity entity) {
    entities.add(entity);
  }
}