package entities;

import java.util.List;

import genetics.AnimalGenetics;

public abstract class Animal extends Entity {
  private final String[] eats; // The entitys' names that this animal eats

  public Animal(String name, AnimalGenetics genetics, Vector position, String[] eats) {
    super(name, genetics, position);
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
  public void moveWithVector(Vector tot) {
    // If no entity of interest is seen, move randomly
    if (tot.getMagnitude() == 0) { tot = Vector.getRandomVector();}
    tot = tot.setMagnitude(getMaxSpeed());
    position = position.add(tot);
  }
  
  public String[] getEats() { return eats; }
  public double getSight() { return ((AnimalGenetics) genetics).getSight(); }
  public double getMaxSpeed() { return ((AnimalGenetics) genetics).getMaxSpeed(); }

  public List<Entity> breed(List<Entity> others) {
    return null;
  }
}