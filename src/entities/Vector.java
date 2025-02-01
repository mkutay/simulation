package entities;
import java.util.List;

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

  public static Vector addVectors(List<Vector> vectors) {
    Vector returnVector = new Vector(0, 0);
    vectors.forEach(returnVector::add);
    return returnVector;
  }

  public double getMagnitude() {
    return Math.hypot(x,y);
  }
  public double getMagnitudeSquared() {
    return x*x + y*y;
  }

  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  public static Vector getRandomVector() {
    return new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);
  }

}
