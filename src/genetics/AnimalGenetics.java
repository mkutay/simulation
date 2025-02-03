package genetics;

public class AnimalGenetics extends Genetics {
  private final int maxLitterSize; // Maximum number of offspring per breeding
  private final double mutationRate; // Rate at which the entity's genetics can mutate
  private final double maxSeed; // Max speed of the entity
  private final double sight; // Range at which the entity can see other entities
  private final Gender gender;
  private final String[] eats; //The names of the entities the animal eats

  public AnimalGenetics(double breedingProbability, int maxLitterSize, int maxAge, int matureAge, double mutationRate, double maxSeed, double sight, Gender gender, double size, String[] eats, String name) {
    super(maxAge, matureAge, breedingProbability, size, name);
    this.maxLitterSize = maxLitterSize;
    this.mutationRate = mutationRate;
    this.maxSeed = maxSeed;
    this.sight = sight;
    this.gender = gender;
    this.eats = eats;
  }

  public int getMaxLitterSize() { return maxLitterSize; }
  public double getMutationRate() { return mutationRate; }
  public double getMaxSpeed() { return maxSeed; }
  public double getSight() { return sight; }
  public Gender getGender() { return gender; }
  public String[] getEats() { return eats; }
}