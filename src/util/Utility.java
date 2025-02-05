package util;

public class Utility {
  // Linear interpolation
  public static double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }
}
