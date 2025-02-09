package util;
import java.util.List;

/**
 * A class that represents a 2D vector.
 * Also used for representing positions of entities in the field.
 */
public class Vector {
  public double x;
  public double y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector add(Vector other) {
    return new Vector(this.x + other.x, this.y + other.y);
  }

  public Vector subtract(Vector other) {
    return new Vector(this.x - other.x, this.y - other.y);
  }

  public Vector multiply(double scalar) {
    return new Vector(this.x * scalar, this.y * scalar);
  }

  public Vector normalize() {
    double magnitude = this.getMagnitude();
    return new Vector(this.x / magnitude, this.y / magnitude);
  }

  public Vector setMagnitude(double magnitude) {
    return this.normalize().multiply(magnitude);
  }

  public double getMagnitude() {
    return Math.hypot(x, y);
  }
  
  public double getMagnitudeSquared() {
    return x * x + y * y;
  }

  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  public double getAngle() {
    return Math.atan2(y, x);
  }
  
  public Vector getRandomPointInRadius(double radius) {
    double angle = Math.random() * Math.PI * 2;
    double distance = Math.random() * radius;
    return new Vector(x + Math.cos(angle) * distance, y + Math.sin(angle) * distance);
  }

  public static Vector getRandomVector() {
    return new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);
  }

  public static Vector addVectors(List<Vector> vectors) {
    Vector returnVector = new Vector(0, 0);
    vectors.forEach(returnVector::add);
    return returnVector;
  }
}
