package entities;
import genetics.AnimalGenetics;

public class Prey extends Animal {
  public Prey(String name, AnimalGenetics genetics, Location location, String[] eats) {
    super(name, genetics, location, eats);
  }
}