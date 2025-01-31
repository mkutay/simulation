package entities;
import java.util.List;

public class Vector {
  public double x;
  public double y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void add(Vector other) {
    this.x += other.x;
    this.y += other.y;
  }

  public void subtract(Vector other) {
    this.x -= other.x;
    this.y -= other.y;
  }

  public void multiply(double scalar) {
    this.x *= scalar;
    this.y *= scalar;
  }

  public double getMagnitude() {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  public void normalize() {
    double magnitude = this.getMagnitude();
    this.x /= magnitude;
    this.y /= magnitude;
  }

  public void rotate180() {
    this.x = -this.x;
    this.y = -this.y;
  }

  public void makeMagnitude(double magnitude) {
    this.normalize();
    this.multiply(magnitude);
  }

  public static Vector addVectors(List<Vector> vectors) {
    Vector returnVector = new Vector(0, 0);
    vectors.forEach(v -> returnVector.add(v));
    return returnVector;
  }

  public static Vector createFromLocations(Location from, Location to) {
    return new Vector(to.x - from.x, to.y - from.y);
  }
}
