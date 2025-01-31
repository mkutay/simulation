package entities;
import genetics.AnimalGenetics;

public class Prey extends Animal {
  private String[] scaredOf;

  public Prey(String name, AnimalGenetics genetics, Location location, String[] eats, String[] scaredOf) {
    super(name, genetics, location, eats);
    this.scaredOf = scaredOf;
  }

  public String[] getScaredOf() { return scaredOf; }
}