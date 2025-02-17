package simulation.quadTree;

import util.Vector;

/**
 * Simple Rectangle record for quadtree sections.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public record Rectangle(double x, double y, double w, double h) {
  /**
   * @param point The point to check inside the rectangle
   * @return True if the point is in the rectangle, false otherwise
   */
  public boolean hasPoint(Vector point) {
    double px = point.x();
    double py = point.y();
    return (px >= x && px <= x + w && py >= y && py <= y + h);
  }

  /**
   * Detects if a given circle is intersecting with this rectangle.
   * @param c The circle to detect collision with.
   * @return True if colliding, false otherwise.
   */
  public boolean intersects(Circle c) {
    // Calculate closest X, Y from circle to rectangle.
    double closestX = Math.max(x, Math.min(c.centre().x(), x + w));
    double closestY = Math.max(y, Math.min(c.centre().y(), y + h));
    double dx = closestX - c.centre().x();
    double dy = closestY - c.centre().y();

    double distanceSquared = dx * dx + dy * dy;
    // Determine if closest point is within the circle or not (i.e. colliding or not):
    return distanceSquared <= c.r() * c.r();
  }
}