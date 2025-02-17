package simulation.quadTree;

import util.Vector;

/**
 * Simple circle record for queryRange in the quadtree.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public record Circle(Vector centre, double r) {
  /**
   * Determines if a point is within the circle or not.
   * Uses magnitude squared as optimisation to prevent a square root call.
   * @param point The point to determine if it's in the circle or not.
   * @return True if the point is in the circle, false otherwise.
   */
  public boolean hasPoint(Vector point) {
    Vector delta = point.subtract(centre);
    return delta.getMagnitudeSquared() < r * r;
  }
}
