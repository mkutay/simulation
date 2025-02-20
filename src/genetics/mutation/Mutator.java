package genetics.mutation;

import simulation.simulationData.Data;

/**
 * Responsible for mutating genetics. Contains methods for mutating a single value
 * in double and int.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Mutator {
  /**
   * Mutate a single value in double.
   * @param value The value to mutate.
   * @param interval The interval of the value (0th index is the minimum, 1st index is the maximum).
   * @param mutationRate The probability of mutation happening.
   * @return The mutated value.
   */
  protected static double singleMutate(double value, double[] interval, double mutationRate) {
    if (Math.random() >= mutationRate) return value;
    // Randomly increase or decrease the value:
    double mutatedValue = value + value * Data.getMutationFactor() * (Math.random() > 0.5 ? 1 : -1);
    return Math.max(interval[0], Math.min(interval[1], mutatedValue));
  }

  /**
   * Mutate a single value in int.
   * @param value The value to mutate.
   * @param interval The interval of the value (0th index is the minimum, 1st index is the maximum).
   * @param mutationRate The probability of mutation happening.
   * @return The mutated value.
   */
  protected static int singleMutate(int value, int[] interval, double mutationRate) {
    if (Math.random() >= mutationRate) return value;
    int mutatedValue = (int) Math.round(value + value * Data.getMutationFactor() * (Math.random() > 0.5 ? 1 : -1));
    return Math.max(interval[0], Math.min(interval[1], mutatedValue));
  }
}