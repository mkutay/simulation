package entities;

import genetics.PlantGenetics;

public class Plant extends Entity {
  PlantGenetics genetics; // Genetics of the plant
  public Plant(int age, String name, PlantGenetics genetics) {
    super(age, name);
    this.genetics = genetics;
  }
}
