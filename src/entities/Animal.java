package entities;

import genetics.AnimalGenetics;

public class Animal extends Entity {
  public Animal(String name, AnimalGenetics genetics, Location location) {
    super(name, genetics, location);
  }
}