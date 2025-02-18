package util;

/**
 * A record that represents a 2D vector with two double values.
 * Also used for representing positions of entities in the field.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public record Vector(double x, double y) {
  /**
   * Add two vectors together.
   */
  public Vector add(Vector other) {
    return new Vector(this.x() + other.x(), this.y() + other.y());
  }

  /**
   * Subtract two vectors.
   */
  public Vector subtract(Vector other) {
    return new Vector(this.x() - other.x(), this.y() - other.y());
  }

  /**
   * Multiply a vector by a scalar.
   */
  public Vector multiply(double scalar) {
    return new Vector(this.x() * scalar, this.y() * scalar);
  }

  /**
   * normalise the vector.
   */
  public Vector normalise() {
    double magnitude = this.getMagnitude();
    return new Vector(this.x() / magnitude, this.y() / magnitude);
  }

  /**
   * Set the magnitude of the vector.
   */
  public Vector setMagnitude(double magnitude) {
    return this.normalise().multiply(magnitude);
  }

  /**
   * Get the magnitude of the vector.
   */
  public double getMagnitude() {
    return Math.hypot(x, y);
  }
  
  /**
   * Get the squared magnitude of the vector.
   */
  public double getMagnitudeSquared() {
    return x * x + y * y;
  }

  /**
   * Return the string representation of the vector.
   */
  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  /**
   * Get the angle of the vector.
   */
  public double getAngle() {
    return Math.atan2(y, x);
  }
  
  /**
   * Get a random point in a radius around this vector.
   */
  public Vector getRandomPointInRadius(double radius) {
    double angle = Math.random() * Math.PI * 2;
    double distance = Math.random() * radius;
    return new Vector(x + Math.cos(angle) * distance, y + Math.sin(angle) * distance);
  }

  /**
   * Get a new vector from an angle. The vector has magnitude of 1.
   */
  public static Vector getVectorFromAngle(double angle) {
    return new Vector(Math.cos(angle), Math.sin(angle));
  }

  /**
   * Get a random vector.
   */
  public static Vector getRandomVector() {
    return new Vector(Math.random() * 2 - 1, Math.random() * 2 - 1);
  }
}
