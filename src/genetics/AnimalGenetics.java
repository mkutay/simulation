package genetics;

public class AnimalGenetics extends Genetics {
  private int maxLitterSize; // Maximum number of offspring per breeding
  private int maxAge; // Maximum age of the entity
  private double matureAge; // Age at which the entity can start breeding
  private double mutationRate; // Rate at which the entity's genetics can mutate
  private double speed; // Speed of the entity
  private double sightRange; // Range at which the entity can see other entities

  public AnimalGenetics(double breedingProbability, int maxLitterSize, int maxAge, double matureAge, double mutationRate, double speed, double sightRange) {
    super(breedingProbability);
    this.maxLitterSize = maxLitterSize;
    this.maxAge = maxAge;
    this.matureAge = matureAge;
    this.mutationRate = mutationRate;
    this.speed = speed;
    this.sightRange = sightRange;
  }
}