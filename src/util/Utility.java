package util;

import simulation.simulationData.Data;

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

  /**
   * Linear interpolation
   */
  public static double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }

  /**
   * Adds a random change in value to a colour
   * @param color the colour to mutate
   * @return the mutated colour
   */
  public static Color mutateColor(Color color, double mutationRate) {
    if (Data.getMutationFactor() < 0 || Data.getMutationFactor()> 1) {
      throw new IllegalArgumentException("mutationFactor must be between 0 and 1");
    }

    int r = mutateChannel(color.getRed(), mutationRate);
    int g = mutateChannel(color.getGreen(), mutationRate);
    int b = mutateChannel(color.getBlue(), mutationRate);

    return new Color(r, g, b);
  }

  /**
   * Adjusts a single RGB value randomly
   * @param value the RGB value to mutate
   * @return the mutated value
   */
  private static int mutateChannel(int value, double mutationRate) {
    if (Math.random() >= mutationRate) return value; // No mutation
    int mutation = (int) (value * Data.getMutationFactor() * (rand.nextDouble() > 0.5 ? 1 : -1));
    return Math.max(0, Math.min(255, value + mutation)); // Clamp between 0 and 255
  }

  /**
   * Generates a new colour by breeding two parent colours.
   * For each RGB component, randomly takes a weighted average
   * between the corresponding components of the parent colours.
   * @param animal The first parent colour.
   * @param mate The second parent colour.
   * @return A new Color object representing the offspring colour.
   */
  public static Color breedColor(Color animal, Color mate) {
    double r = Math.random();
    int red = (int) Math.round(r * animal.getRed() + (1 - r) * mate.getRed());
    r = Math.random();
    int green = (int) Math.round(r * animal.getGreen() + (1 - r) * mate.getGreen());
    r = Math.random();
    int blue = (int) Math.round(r * animal.getBlue() + (1 - r) * mate.getBlue());
    return new Color(red, green, blue);
  }
}
