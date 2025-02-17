package entities.generic;

import entities.Predator;
import simulation.Field;

import java.util.List;
import java.util.function.Predicate;

/**
 * Handles animal behaviour
 * All animal behaviour, predator or prey, is identical. They move to food and away from things that eat them.
 */
public class AnimalBehaviourController {
    private final Animal animal;

    public AnimalBehaviourController(Animal animal) {
        this.animal = animal;
    }

    /**
     * Updates the behaviour of the animal, specifically for movement.
     * @param field The field the animal is in.
     * @param nearbyEntities The entities in the sight radius of the animal.
     * @param deltaTime The delta time.
     */
    public void updateBehaviour(Field field, List<Entity> nearbyEntities, double deltaTime) {
        boolean isHungry = animal.hungerController.isHungry();
        boolean isDyingOfHunger = animal.hungerController.isDyingOfHunger(); // Extreme case for prey to prioritise food over fleeing from predators.
        animal.isAsleep = false;

        if (isHungry) animal.hungerController.eat(nearbyEntities);

        if (!isDyingOfHunger) { // If not dying of hunger, attempt to flee from predators.
            if (fleeFromPredators(nearbyEntities, deltaTime)){ return; } //If fleeing, stop other behaviour
        }

        //If not hungry, its nighttime and no predator is nearby, sleep (do nothing)
        if (!isHungry && !field.isDay()) { animal.isAsleep = true; return; }

        boolean movingToFood = false;
        if (isHungry && !animal.isMovingToMate) { // If is hungry and not currently attempting to mate
            movingToFood = animal.movementController.moveToNearestFood(nearbyEntities, deltaTime);
        }

        if (!movingToFood) { // If not moving to food and not hungry, look for mate
            animal.isMovingToMate = animal.movementController.moveToNearestMate(nearbyEntities, deltaTime);
            if (!animal.isMovingToMate) animal.movementController.wander(field, deltaTime); // If cant find mate, wander.
        }
    }

    /**
     * Searches for nearby predators and flees if found
     * @return true if fleeing, false otherwise
     */
    private boolean fleeFromPredators(List<Entity> nearbyEntities, double deltaTime) {
        // Find the nearest predator:
        Predicate<Entity> condition = e -> e instanceof Predator p && p.getHungerController().canEat(animal);
        Predator nearestPredator = (Predator) animal.movementController.getNearestEntity(nearbyEntities, condition);

        if (nearestPredator != null) { // If a predator is found, flee!
            animal.movementController.fleeFromEntity(nearestPredator, deltaTime); // Prioritise fleeing from predators
            return true; // Stop other behaviours from occurring
        }
        return false;
    }
}
