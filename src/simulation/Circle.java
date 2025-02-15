package simulation;

import util.Vector;

/**
 * Simple circle class for queryRange in quadtree
 */
public class Circle{
    public Vector center;
    public double r;
    public Circle(double x, double y, double radius){
        center = new Vector(x, y);
        r = radius;
    }

    /**
     * Determines if a point is within the circle or not
     * Uses magnitude squared as optimisation to prevent square root call
     * @param point the point to determine if it's in the circle or not
     * @return true if the point is in the circle, false otherwise
     */
    public boolean hasPoint(Vector point) {
        Vector delta = point.subtract(center);
        return delta.getMagnitudeSquared() < r * r;
    }
}
