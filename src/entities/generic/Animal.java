package entities.generic;

import java.util.ArrayList;
import java.util.List;

import entities.Vector;
import genetics.AnimalGenetics;
import simulation.Field;

public abstract class Animal extends Entity {
  protected AnimalGenetics genetics; //Re-cast to AnimalGenetics
  public Animal(AnimalGenetics genetics, Vector position) {
    super(genetics, position);
    this.genetics = genetics;
  }

  /**
   * The animal will move according to the total vector that is given.
   * Only the direction of the vector is considered, not the magnitude.
   * And, the animal will move according to its maximum speed. 
   * TODO: Might change this so that the animal moves according to the
   * TODO: magnitude of the vector with inverse relationship.
   * @param tot The total vector that the animal will use to move.
   */
  public void moveWithVector(Vector tot) {
    // If no entity of interest is seen, move randomly
    if (tot.getMagnitude() == 0) { tot = Vector.getRandomVector(); }
    tot = tot.setMagnitude(getMaxSpeed());
    position = position.add(tot);
  }

  /**
   * TODO: Optimise when necessary
   * TODO: Test this method -- create a test unit
   * @return Returns all entities in the field in the animals sight radius
   */
  public ArrayList<Entity> searchNearbyEntities(Field field){
    ArrayList<Entity> foundEntities = new ArrayList<>();
    ArrayList<Entity> fieldEntities = field.getEntities();

    for (Entity e : fieldEntities) {
      Vector entityPosition = e.getPosition();
      double distanceSquared = position.subtract(entityPosition).getMagnitudeSquared();
      double sightRadius = genetics.getSight();
      if (distanceSquared <= sightRadius * sightRadius) { // If can see other entity
        foundEntities.add(e);
      }
    }

    return foundEntities;
  }
  
  public String[] getEats() { return genetics.getEats(); }
  public double getMaxSpeed() { return genetics.getMaxSpeed(); }

  public List<Entity> breed(List<Entity> others) {
    return null;
  }
}