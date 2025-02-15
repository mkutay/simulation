package simulation;

import util.Vector;

/**
 * Simple Rectangle class for quadtree sections
 */
public class Rectangle{
    public double x, y, w, h; //(x, y) is top left
    public Rectangle(double x, double y, double width, double height){
        this.x = x;
        this.y = y;
        w = width;
        h = height;
    }

    /**
     * @param point The point to check inside the rectangle
     * @return True if the point is in the rectangle, false otherwise
     */
    public boolean hasPoint(Vector point){
        double px = point.x();
        double py = point.y();
        return (px >= x && px <= x + w && py >= y && py <= y + h);
    }

    /**
     * Detects if a given circle is intersecting with this rect
     * @param c the circle to detect collision with
     * @return True if colliding, false otherwise
     */
    public boolean intersects(Circle c){ //circle-rect intersection
        //calculate closest X,Y from circle to rectangle
        double closestX = Math.max(x, Math.min(c.center.x(), x + w));
        double closestY = Math.max(y, Math.min(c.center.y(), y + h));
        double dx = closestX - c.center.x();
        double dy = closestY - c.center.y();

        double distanceSquared = dx * dx + dy * dy;
        return distanceSquared <= c.r * c.r; //determine if closest point is within the circle or not (ie colliding or not)
    }
}
