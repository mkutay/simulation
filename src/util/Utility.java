package util;

import java.awt.*;
import java.util.Random;

/**
 * Utility class for various mathematical operations, constants, and helpers.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Utility {
  public static final double EPSILON = 1e-4; // Epsilon value for floating point comparisons
  private static final Random rand = new Random();

  public static double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }

  /**
   * Adds a random change in value to a colour
   * @param color the colour to mutate
   * @param mutationFactor the amount to mutate the colour
   * @return the mutated colour
   */
  public static Color mutateColor(Color color, double mutationFactor) {
    if (mutationFactor < 0 || mutationFactor > 1) {
      throw new IllegalArgumentException("mutationFactor must be between 0 and 1");
    }

    int r = mutateChannel(color.getRed(), mutationFactor);
    int g = mutateChannel(color.getGreen(), mutationFactor);
    int b = mutateChannel(color.getBlue(), mutationFactor);

    return new Color(r, g, b);
  }

  /**
   * Adjusts a single RGB value randomly
   * @param value the RGB value to mutate
   * @param mutationFactor the amount to mutate the value by
   * @return the mutated value
   */
  private static int mutateChannel(int value, double mutationFactor) {
    int range = (int) (mutationFactor * 255); // Max shift allowed
    int mutation = rand.nextInt(range * 2 + 1) - range; // Shift in both directions
    return Math.max(0, Math.min(255, value + mutation)); // Clamp between 0 and 255
  }
}
