package util;

import simulation.simulationData.Data;

import java.awt.Color;

/**
 * Utility class for various mathematical operations, constants, and helpers.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Utility {
  public static final double EPSILON = 1e-4; // Epsilon value for floating point comparisons

  /**
   * Linear interpolation.
   */
  public static double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }

  /**
   * Adds a random change in value to a colour.
   * Uses pre-defined mutation factor from the simulation data.
   * @param color The colour to mutate.
   * @return The mutated colour.
   */
  public static Color mutateColor(Color color, double mutationRate) {
    return Utility.mutateColor(color, mutationRate, Data.getMutationFactor());
  }

  /**
   * Adds a random change in value to a colour.
   * @param color The colour to mutate.
   * @param mutationFactor How drastic the mutation is.
   * @return The mutated colour.
   */
  public static Color mutateColor(Color color, double mutationRate, double mutationFactor) {
    if (mutationFactor < 0 || mutationFactor > 1) {
      throw new IllegalArgumentException("mutationFactor must be between 0 and 1");
    }

    int r = mutateChannel(color.getRed(), mutationRate, mutationFactor);
    int g = mutateChannel(color.getGreen(), mutationRate, mutationFactor);
    int b = mutateChannel(color.getBlue(), mutationRate, mutationFactor);

    return new Color(r, g, b);
  }

  /**
   * Adjusts a single RGB value randomly.
   * @param value The RGB value to mutate.
   * @param mutationFactor How drastic the mutation is.
   * @return The mutated value.
   */
  private static int mutateChannel(int value, double mutationRate, double mutationFactor) {
    if (Math.random() >= mutationRate) return value; // No mutation.
    int mutation = (int) (value * mutationFactor * (Math.random() > 0.5 ? 1 : -1));
    return Math.max(0, Math.min(255, value + mutation)); // Clamp between 0 and 255.
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
