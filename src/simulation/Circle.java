package simulation;

import util.Vector;

/**
 * Simple circle class for queryRange in the quadtree.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Circle{
  public Vector center; // Center of the circle
  public double r; // Radius of the circle

  /**
   * Constructor for the circle.
   * @param x X coordinate of the center.
   * @param y Y coordinate of the center.
   * @param radius Radius of the circle.
   */
  public Circle(double x, double y, double radius) {
    center = new Vector(x, y);
    r = radius;
  }

  /**
   * Determines if a point is within the circle or not.
   * Uses magnitude squared as optimisation to prevent a square root call.
   * @param point The point to determine if it's in the circle or not.
   * @return True if the point is in the circle, false otherwise.
   */
  public boolean hasPoint(Vector point) {
    Vector delta = point.subtract(center);
    return delta.getMagnitudeSquared() < r * r;
  }
}
