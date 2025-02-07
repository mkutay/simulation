package entities.generic;

import java.util.ArrayList;
import java.util.List;

import util.Utility;
import util.Vector;
import genetics.AnimalGenetics;
import simulation.Field;

public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; // Re-cast to AnimalGenetics
  protected int foodLevel;
  
  protected final AnimalMovement movement;
  protected final AnimalEating eating;
  protected final AnimalBreeding breeding;

  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
    this.foodLevel = genetics.getMaxFoodLevel();
    movement = new AnimalMovement(this);
    eating = new AnimalEating(this);
    breeding = new AnimalBreeding(this);
  }

  /**
   * TODO: Optimise when necessary
   * @param entities The entities that will be searched through.
   * @return Returns all entities in the field in the animals sight radius, except itself.
   */
  public ArrayList<Entity> searchNearbyEntities(ArrayList<Entity> entities) {
    ArrayList<Entity> foundEntities = new ArrayList<>();
    if (entities == null) return foundEntities;

    for (Entity e : entities) {
      double distanceSquared = e.getPosition().subtract(position).getMagnitudeSquared();
      double sightRadius = genetics.getSight();
      // Epsilon is used for floating point comparison
      if (distanceSquared - sightRadius * sightRadius <= Utility.EPSILON && e != this) {
        // If can see other entity and it is not itself
        foundEntities.add(e);
      }
    }

    return foundEntities;
  }

  /**
   * @param entities The entities that will be searched through.
   * @return All animals of the same species as this animal.
   */
  protected List<Animal> getSameSpecies(List<Entity> entities) {
    List<Animal> sameSpecies = new ArrayList<>();
    entities.forEach(entity -> {
      if (entity instanceof Animal animal && animal.getName().equals(getName())) {
        sameSpecies.add(animal);
      }
    });
    return sameSpecies;
  }

  @Override
  public void update(Field field, double deltaTime) {
    super.update(field, deltaTime);
    movement.simpleWander(field, deltaTime);
  }

  // Getters:
  public String[] getEats() { return genetics.getEats(); }
  public double getMaxSpeed() { return genetics.getMaxSpeed(); }
}