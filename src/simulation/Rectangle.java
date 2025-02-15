package simulation;

import util.Vector;

/**
 * Simple Rectangle class for quadtree sections.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Rectangle{
  public double x, y, w, h; // (x, y) is top left

  public Rectangle(double x, double y, double width, double height){
    this.x = x;
    this.y = y;
    this.w = width;
    this.h = height;
  }

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
   * Detects if a given circle is intersecting with this rectangle
   * @param c The circle to detect collision with.
   * @return True if colliding, false otherwise.
   */
  public boolean intersects(Circle c) {
    // Calculate closest X, Y from circle to rectangle.
    double closestX = Math.max(x, Math.min(c.center.x(), x + w));
    double closestY = Math.max(y, Math.min(c.center.y(), y + h));
    double dx = closestX - c.center.x();
    double dy = closestY - c.center.y();

    double distanceSquared = dx * dx + dy * dy;
    return distanceSquared <= c.r * c.r; // Determine if closest point is within the circle or not (i.e. colliding or not).
  }
}