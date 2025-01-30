package genetics;

/**
 * Represents a set of genetic intervals for an animal.
 * This is used when parsing the JSON file for each species of animals.
 */
public class AnimalData extends EntityData{
  public int[] maxLitterSize; // Maximum number of offspring per breeding
  public double[] mutationRate; // Rate at which the entity's genetics can mutate
  public double[] speed; // Speed of the entity
  public double[] sight; // Range at which the entity can see other entities
  public String[] eats; // List of entities that this entity can eat

  public AnimalGenetics generateRandomGenetics() {
    return new AnimalGenetics(
      generateRandomNumberBetween(multiplyingRate[0], multiplyingRate[1]),
      generateRandomNumberBetween(maxLitterSize[0], maxLitterSize[1]),
      generateRandomNumberBetween(maxAge[0], maxAge[1]),
      generateRandomNumberBetween(matureAge[0], matureAge[1]),
      generateRandomNumberBetween(mutationRate[0], mutationRate[1]),
      generateRandomNumberBetween(speed[0], speed[1]),
      generateRandomNumberBetween(sight[0], sight[1]),
      getRandomGender(),
      generateRandomNumberBetween(size[0], size[1])
    );
  }

  private Gender getRandomGender() {
    return Math.random() <= 0.5 ? Gender.MALE : Gender.FEMALE;
  }
}