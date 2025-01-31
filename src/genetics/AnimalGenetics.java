package genetics;

public class AnimalGenetics extends Genetics {
  private int maxLitterSize; // Maximum number of offspring per breeding
  private double mutationRate; // Rate at which the entity's genetics can mutate
  private double maxSeed; // Max speed of the entity
  private double sight; // Range at which the entity can see other entities
  private Gender gender;

  public AnimalGenetics(double breedingProbability, int maxLitterSize, int maxAge, int matureAge, double mutationRate, double maxSeed, double sight, Gender gender, double size) {
    super(maxAge, matureAge, breedingProbability, size);
    this.maxLitterSize = maxLitterSize;
    this.mutationRate = mutationRate;
    this.maxSeed = maxSeed;
    this.sight = sight;
    this.gender = gender;
  }

  public int getMaxLitterSize() { return maxLitterSize; }
  public double getMutationRate() { return mutationRate; }
  public double getMaxSpeed() { return maxSeed; }
  public double getSight() { return sight; }
  public Gender getGender() { return gender; }
}