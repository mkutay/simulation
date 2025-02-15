package entities.generic;

import simulation.simulationData.Data;

import java.util.Arrays;
import java.util.List;

public class AnimalHungerController {
    private final Animal animal;
    protected double foodLevel; // The current food level of the animal (as a ratio between 0 and 1)
    private boolean hasEaten = false; // Stores if the animal has eaten at least once or not -- used for breeding

    public final static double HUNGRY_THRESHOLD = 0.5; // Will look for food at 50% hunger
    public final static double DYING_OF_HUNGER_THRESHOLD = 0.2; // Will prioritise looking for food at 20% hunger

    public AnimalHungerController(Animal animal) {
        this.animal = animal;
        foodLevel = 0.5; //Spawn with 50% food
    }

    /**
     * Attempts to eat any colliding entities.
     * @param nearbyEntities The entities in the sight radius of this animal.
     */
    public void eat(List<Entity> nearbyEntities) {
        if (nearbyEntities == null) return;

        for (Entity entity : nearbyEntities) {
            if (!(this.canEat(entity) && animal.isColliding(entity))) continue;

            double entitySizeRatio = (double) entity.getSize() / animal.getSize();
            double foodValue = (entity instanceof Animal ? Data.getFoodValueForAnimals() : Data.getFoodValueForPlants());
            double foodQuantity = entitySizeRatio * foodValue;
            foodLevel += foodQuantity;
            this.hasEaten = true; // Mark this animal as having eaten at least once -- used for breeding.
            entity.setDead();
        }

        foodLevel = Math.min(foodLevel, 1); // Clamp food from exceeding max food of animal, which is 1.
    }


    /**
     * Sets animal as dead if it has no food, decrements food level proportion to deltaTime.
     * @param numberOfOffsprings The number of offsprings the animal had in the current simulation step.
     * @param deltaTime Delta time.
     */
    public void handleHunger(double deltaTime, int numberOfOffsprings) {
        // Decrease food level based on current distance travelled, which is proportional to speed.
        double distanceTraveled = animal.movementController.getDistanceTravelled();
        double hungerDrainPerTick = Data.getAnimalHungerDrain() * distanceTraveled * deltaTime; // * genetics.getSight(); // TODO: Sight affects hunger drain as balancing system

        foodLevel -= hungerDrainPerTick;
        foodLevel -= numberOfOffsprings / (numberOfOffsprings + 1 / Data.getAnimalBreedingCost()); // Food cost for breeding
        if (foodLevel <= 0) animal.setDead();
    }

    /**
     * Checks if this animal can eat a specified entity.
     * @param entity The entity to check if this animal can eat.
     * @return True if this animal can eat the entity, false otherwise.
     */
    public boolean canEat(Entity entity) {
        return Arrays.asList(animal.genetics.getEats()).contains(entity.getName()) && entity.isAlive();
    }

    public boolean isHungry(){
        return foodLevel <= HUNGRY_THRESHOLD;
    }

    public boolean isDyingOfHunger() {
        return foodLevel <= DYING_OF_HUNGER_THRESHOLD;
    }

    public boolean hasEaten(){
        return hasEaten;
    }

    public double getFoodLevel() {
        return foodLevel;
    }
}
