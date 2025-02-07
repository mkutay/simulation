package util;

public class Utility {
  public static final double EPSILON = 1e-4;
  // Linear interpolation
  public static double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }
}
