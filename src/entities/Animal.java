package entities;

import genetics.AnimalGenetics;

public class Animal extends Entity {
  private String[] eats; // The entitys' names that this animal eats
  private double speed; // The current speed of the animal

  public Animal(String name, AnimalGenetics genetics, Location location, String[] eats) {
    super(name, genetics, location);
    this.eats = eats;
    speed = 0;
  }

  /**
   * The animal will move according to the total vector that is given.
   * @param tot The total vector that the animal will use to move.
   */
  public void move(Vector tot) {
    
  }

  /**
   * @return The range of sight that the animal can see.
   */
  public double getSight() {
    AnimalGenetics ag = (AnimalGenetics) genetics;
    return ag.getSight();
  }

  public String[] getEats() { return eats; }
}