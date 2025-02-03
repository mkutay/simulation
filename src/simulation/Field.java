package simulation;
import java.util.ArrayList;
import entities.*;
import entities.generic.Entity;

public class Field {
    private final int width; // Width of the field
    private final int height; // Height of the field

    private final ArrayList<Prey> preys;
    private final ArrayList<Predator> predators;
    private final ArrayList<Plant> plants;

    public Field(FieldBuilder fieldBuilder) {
        this.width = fieldBuilder.getWidth();
        this.height = fieldBuilder.getHeight();

        preys = fieldBuilder.getPreys();
        predators = fieldBuilder.getPredators();
        plants = fieldBuilder.getPlants();
    }

    /**
    * Put an entity in-bounds if it is out of bounds (that is the field width and height).
    * @param entity The entity that will be moved.
    */
    public void putInBounds(Entity entity) {
        Vector entityPos = entity.getPosition();
        if (entityPos.x < 0) entityPos.x = 0;
        if (entityPos.y < 0) entityPos.y = 0;
        if (entityPos.x >= width) entityPos.x = width;
        if (entityPos.y >= height) entityPos.y = height;
    }

    /**
    * TODO: may not be necessary
    * @return Whether the field is viable or not, for to check if the simulation should continue.
    */
    public boolean isViable() {
        return !preys.isEmpty() && !predators.isEmpty();
    }

    public ArrayList<Prey> getPreys() {
        return preys;
    }

    public ArrayList<Predator> getPredators() {
        return predators;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    /**
    * Filter out the entities that are not alive.
    */
    public void removeDeadEntities() {
        plants.removeIf(plant -> !plant.isAlive());
        preys.removeIf(predator -> !predator.isAlive());
        predators.removeIf(predator -> !predator.isAlive());
    }

    /**
     * @return the number of entities in the field
     */
    public int getTotalNumEntities() {
        return preys.size() + predators.size() + plants.size();
    }
}