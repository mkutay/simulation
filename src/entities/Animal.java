package entities;

import genetics.AnimalGenetics;

public class Animal extends Entity {
  private String[] eats; // The entitys' names that this animal eats

  public Animal(String name, AnimalGenetics genetics, Location location, String[] eats) {
    super(name, genetics, location);
    this.eats = eats;
  }

  /**
   * The animal will move according to the total vector that is given.
   * Only the direction of the vector is considered, not the magnitude.
   * And, the animal will move according to its maximum speed. 
   * TODO: Might change this so that the animal moves according to the
   * TODO: magnitude of the vector with inverse relationship.
   * @param tot The total vector that the animal will use to move.
   */
  public void move(Vector tot) {
    if (tot.getMagnitude() == 0) {
      tot = Vector.getRandomVector();
      tot.makeMagnitude(this.getMaxSpeed());
      System.out.println("Random");
    } else {
      tot.makeMagnitude(this.getMaxSpeed());
    }
    location.addVector(tot);
  }

  /**
   * @return The range of sight that the animal can see.
   */
  public double getSight() {
    AnimalGenetics ag = (AnimalGenetics) genetics;
    return ag.getSight();
  }

  public String[] getEats() { return eats; }

  public double getMaxSpeed() {
    return ((AnimalGenetics) genetics).getMaxSpeed();
  }
}