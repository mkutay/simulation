package entities.generic;

import java.util.Arrays;
import java.util.List;

public class AnimalEating {
  private Animal animal;

  public AnimalEating(Animal animal) {
    this.animal = animal;
  }

  /**
   * Checks if the animal can eat the entity.
   * @param entity The entity to check if it can be eaten.
   * @return True if the entity can be eaten, false otherwise.
   */
  public boolean canEat(Entity entity) {
    return Arrays.asList(animal.getEats()).contains(entity.getName());
  }

  /**
   * Perform the eating action for the animal.
   * @param animal The animal that is eating.
   * @param nearbyEntities The entities that are nearby the animal.
   */
  public void performEating(List<Entity> nearbyEntities) {
    if (nearbyEntities == null) return;
    for (Entity entity : nearbyEntities) {
      // The plant may be dead in the current step, must check if dead first.
      if (entity.isAlive() && canEat(entity) && Entity.isColliding(animal, entity)) {
        animal.foodLevel += entity.getSize() * 3;
        entity.setDead();
      }
    }
    animal.foodLevel = Math.min(animal.foodLevel, animal.genetics.getMaxFoodLevel());

    animal.foodLevel -= 2;
    if (animal.foodLevel <= 0) {
      animal.setDead();
    }
  }
}
