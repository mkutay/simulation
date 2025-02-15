package genetics.mutation;

/**
 * Responsible for mutating genetics.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Mutator {
  /**
   * Mutate a single value.
   * @param value The value to mutate.
   * @param interval The interval of the value (0th index is the minimum, 1st index is the maximum).
   * @param mutationRate The probability of mutation happening.
   * @param mutationFactor By how much the value can change in either direction.
   * @return The mutated value.
   */
  protected static double singleMutate(double value, double[] interval, double mutationRate, double mutationFactor) {
    if (Math.random() >= mutationRate) return value;
    double mutatedValue = value + value * mutationFactor * (Math.random() > 0.5 ? 1 : -1); // Randomly increase or decrease the value.
    return Math.max(interval[0], Math.min(interval[1], mutatedValue));
  }

  protected static int singleMutate(int value, int[] interval, double mutationRate, double mutationFactor) {
    if (Math.random() >= mutationRate) return value;
    int mutatedValue = (int) Math.round(value + value * mutationFactor * (Math.random() > 0.5 ? 1 : -1));
    return Math.max(interval[0], Math.min(interval[1], mutatedValue));
  }
}