package entities;
import genetics.AnimalGenetics;

public class Predator extends Animal {
  public Predator(String name, AnimalGenetics genetics, Location location, String[] eats) {
    super(name, genetics, location, eats);
  }
}
