package genetics;

public class AnimalGenetics extends Genetics {
  private int maxLitterSize; // Maximum number of offspring per breeding
  private double mutationRate; // Rate at which the entity's genetics can mutate
  private double speed; // Speed of the entity
  private double sight; // Range at which the entity can see other entities

  public AnimalGenetics(double breedingProbability, int maxLitterSize, int maxAge, int matureAge, double mutationRate, double speed, double sight) {
    super(maxAge, matureAge, breedingProbability);
    this.maxLitterSize = maxLitterSize;
    this.mutationRate = mutationRate;
    this.speed = speed;
    this.sight = sight;
  }

  public int getMaxLitterSize() { return maxLitterSize; }
  public double getMutationRate() { return mutationRate; }
  public double getSpeed() { return speed; }
  public double getSight() { return sight; }
}