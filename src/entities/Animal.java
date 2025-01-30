package entities;

import genetics.AnimalGenetics;

public class Animal extends Entity {
  private String[] eats; // The food that this animal eats

  public Animal(String name, AnimalGenetics genetics, Location location, String[] eats) {
    super(name, genetics, location);
    this.eats = eats;
  }
}