package entities;

import java.util.List;

import entities.generic.Entity;
import genetics.PlantGenetics;

public class Plant extends Entity {
  public Plant(PlantGenetics genetics, Vector location) {
    super(genetics, location);
  }

  public List<Plant> multiply() {
    return null;
  }
}
