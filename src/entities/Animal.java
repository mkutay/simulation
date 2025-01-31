package entities;

import genetics.AnimalGenetics;

public class Animal extends Entity {
  private String[] eats; // The entitys' names that this animal eats

  public Animal(String name, AnimalGenetics genetics, Location location, String[] eats) {
    super(name, genetics, location);
    this.eats = eats;
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