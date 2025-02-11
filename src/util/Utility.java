package util;

/**
 * Utility class for various mathematical operations, constants, and helpers.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Utility {
  public static final double EPSILON = 1e-4; // Epsilon value for floating point comparisons

  /**
   * Linear interpolation
   */
  public static double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }
}
