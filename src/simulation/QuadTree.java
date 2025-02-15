package simulation;

import java.awt.Color;
import java.util.ArrayList;

import entities.generic.Entity;
import graphics.Display;

/**
 * Quadtree implementation for performance optimisation of entity searching.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class QuadTree {
  public Rectangle rect; // The rectangle defining the area this quadtree occupies.

  private int currentCapacity = 0; // How many entities this quadtree is currently storing.
  private final int capacity; // The max allowed number of entities to store.

  private boolean hasSubdivided = false; // Whether this quadtree has been subdivided into 4 children quadtrees yet.
  private final Entity[] entities; // The entities stored in this quadtree.

  // Child quadtrees:
  private QuadTree topLeftTree;
  private QuadTree topRightTree;
  private QuadTree bottomLeftTree;
  private QuadTree bottomRightTree;

  public QuadTree(Rectangle rect, int maxCapacity){
    this.rect = rect;
    capacity = maxCapacity;
    entities = new Entity[capacity];
  }

  /**
   * Clears all data stored in the quadtree
   * Used to re-add entities every update
   */
  public void clear() {
    for (int i = 0; i < currentCapacity; i++) {
      entities[i] = null;
    }
    currentCapacity = 0;
    topLeftTree = null;
    topRightTree = null;
    bottomLeftTree = null;
    bottomRightTree = null;
    hasSubdivided = false;
  }

  /**
   * Subdivide this quadtree into 4 children quadtrees.
   */
  public void subdivide() {
    Rectangle topLeft = new Rectangle(rect.x, rect.y, rect.w / 2, rect.h / 2);
    Rectangle topRight = new Rectangle(rect.x + rect.w / 2, rect.y, rect.w / 2, rect.h / 2);
    Rectangle bottomLeft = new Rectangle(rect.x, rect.y + rect.h / 2, rect.w / 2, rect.h / 2);
    Rectangle bottomRight = new Rectangle(rect.x + rect.w / 2, rect.y + rect.h / 2, rect.w / 2, rect.h / 2);

    topLeftTree = new QuadTree(topLeft, capacity);
    topRightTree = new QuadTree(topRight, capacity);
    bottomLeftTree = new QuadTree(bottomLeft, capacity);
    bottomRightTree = new QuadTree(bottomRight, capacity);
    hasSubdivided = true;
  }

  /**
   * Add an entity to this quadtree, or a child quadtree if there exists any
   * @param entity the entity to add to the quadtree/child quadtree
   */
  public void insert(Entity entity){
    if (rect.hasPoint(entity.getPosition())) {
      if (currentCapacity < capacity) {
        entities[currentCapacity] = entity;
        currentCapacity++;
      } else {
        if (!hasSubdivided) subdivide();
        topLeftTree.insert(entity);
        topRightTree.insert(entity);
        bottomLeftTree.insert(entity);
        bottomRightTree.insert(entity);
      }
    }
  }

  /**
   * The main query function - Queries the quadtree and adds all entities found in the queryRange.
   * Wraps a list to use queryInternal on, makes for a simpler interface to query.
   * @param queryRange The range to query the quad tree.
   * @return A list of entities found in the given range.
   */
  public ArrayList<Entity> query(Circle queryRange){
    ArrayList<Entity> foundEntities = new ArrayList<>();
    queryInternal(queryRange, foundEntities);
    return foundEntities;
  }

  /**
   * Queries the quadtree and adds all entities found in the queryRange to a list foundEntities.
   * We require an input list to modify as an optimisation to prevent redundant list deep copies.
   * @param queryRange The range to query the quad tree.
   * @param foundEntities The list to add the found entities to.
   */
  private void queryInternal(Circle queryRange, ArrayList<Entity> foundEntities) {
    if (!rect.intersects(queryRange)) {
      return;
    }

    // Check entities stored in this node
    for (Entity entity : entities) {
      if (entity != null && queryRange.hasPoint(entity.getPosition())) {
        foundEntities.add(entity);
      }
    }

    // Recursively query children
    if (hasSubdivided) {
      topLeftTree.queryInternal(queryRange, foundEntities);
      topRightTree.queryInternal(queryRange, foundEntities);
      bottomLeftTree.queryInternal(queryRange, foundEntities);
      bottomRightTree.queryInternal(queryRange, foundEntities);
    }
  }

  /**
   * Draws the quadtree.
   * Used only for debug purposes, but it just looks really cool.
   */
  public void draw(Display display, double scale){
    display.drawRectangle((int) (rect.x / scale), (int) (rect.y / scale), (int) (rect.w / scale), (int) (rect.h / scale), Color.GRAY, false);
    if (hasSubdivided){
      topLeftTree.draw(display, scale);
      topRightTree.draw(display, scale);
      bottomLeftTree.draw(display, scale);
      bottomRightTree.draw(display, scale);
    }
  }
}