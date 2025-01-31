package entities;

import java.util.List;

import genetics.PlantGenetics;

public class Plant extends Entity {
  public Plant(String name, PlantGenetics genetics, Location location) {
    super(name, genetics, location);
  }

  public double getSight() { return 0; }

  public List<Plant> multiply() {
    return null;
  }
}
