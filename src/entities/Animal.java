package entities;

import genetics.AnimalGenetics;

public class Animal extends Entity {
  AnimalGenetics genetics; // Genetics of the animal
  public Animal(int age, String name, AnimalGenetics genetics) {
    super(age, name);
    this.genetics = genetics;
  }
}