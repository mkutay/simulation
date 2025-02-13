package entities.generic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import entities.*;
import simulation.Field;
import simulation.simulationData.*;
import util.Vector;
import genetics.AnimalGenetics;

class AnimalTest {
  private Animal animal;
  private AnimalGenetics genetics;
  private Data data;
  private Field field;

  @BeforeEach
  void setUp() {
    this.data = new Data();
    this.genetics = data.getPredatorsData()[0].generateRandomGenetics();
    this.animal = new Predator(genetics, new Vector(50, 50));
    this.field = new Field(100, 100);
  }

  // TODO: This test assumes that the given data is in order that is currently written in.
  // TODO: Basically, doesn't check if the animal can actually eat the entity.
  @Test
  void testEat_CanEatAndColliding() {
    // First update, so that the food level is not at max.
    animal.update(field, 1);
    double initialFoodLevel = animal.getFoodLevel();
    Prey prey = new Prey(data.getPreysData()[0].generateRandomGenetics(), animal.getPosition());
    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(prey);
  
    animal.eat(nearbyEntities);
  
    assertFalse(prey.isAlive());
    assertTrue(animal.getFoodLevel() > initialFoodLevel);
  }

  @Test
  void testEat_CanEatAndCollidingSecond() {
    // First update, so that the food level is not at max.
    animal.update(field, 1);
    double initialFoodLevel = animal.getFoodLevel();
    Prey prey = new Prey(data.getPreysData()[0].generateRandomGenetics(), animal.getPosition().add(new Vector(genetics.getSize(), 0)));
    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(prey);
  
    animal.eat(nearbyEntities);
  
    assertFalse(prey.isAlive());
    assertTrue(animal.getFoodLevel() > initialFoodLevel);
  }
  
  @Test
  void testEat_ShouldntEatWhatItCannotEat() {
    Entity plant = new Plant(data.getPlantsData()[0].generateRandomGenetics(), new Vector(50, 50));
    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(plant);
  
    animal.eat(nearbyEntities);
  
    assertTrue(plant.isAlive());
  }
  
  @Test
  void testEat_NotColliding() {
    Prey prey = new Prey(data.getPreysData()[0].generateRandomGenetics(), animal.getPosition().add(new Vector(genetics.getSight() + 1, 0)));
    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(prey);
  
    animal.eat(nearbyEntities);
  
    assertTrue(prey.isAlive());
  }
  
  @Test
  void testEat_EntityAlreadyDead() {
    double initialFoodLevel = animal.getFoodLevel();
    Prey prey = new Prey(data.getPreysData()[0].generateRandomGenetics(), animal.getPosition());
    prey.setDead();
    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(prey);
  
    animal.eat(nearbyEntities);
  
    assertFalse(prey.isAlive());
    assertTrue(animal.getFoodLevel() == initialFoodLevel);
  }
  
  @Test
  void testEat_NullList() {
    double initialFoodLevel = animal.getFoodLevel();
    animal.eat(null);
    assertTrue(animal.isAlive());
    assertTrue(animal.getFoodLevel() == initialFoodLevel);
  }

  @Test
  void testMoveToNearestFood_NoNearbyEntities() {
    double initialFoodLevel = animal.getFoodLevel();
    boolean movedToFood = animal.moveToNearestFood(new ArrayList<>(), 1);
    assertFalse(movedToFood);
    assertEquals(initialFoodLevel, animal.getFoodLevel());
  }

  @Test
  void testMoveToNearestFood_FoundSingleFood() {
    Vector initialPosition = animal.getPosition();

    Prey prey = new Prey(data.getPreysData()[0].generateRandomGenetics(), new Vector(55, 50));
    List<Entity> entities = new ArrayList<>();
    entities.add(prey);

    boolean movedToFood = animal.moveToNearestFood(entities, 1);
    assertTrue(movedToFood);
    assertNotEquals(initialPosition, animal.getPosition()); // Position should change
  }

  @Test
  void testMoveToNearestFood_ChoosesNearestFood() {
    Vector initialPosition = animal.getPosition();
    Vector firstVector = new Vector(52, 50);
    Vector secondVector = new Vector(50, 55);
    Prey nearPrey = new Prey(data.getPreysData()[0].generateRandomGenetics(), firstVector);
    Prey farPrey = new Prey(data.getPreysData()[0].generateRandomGenetics(), secondVector);
    List<Entity> entities = new ArrayList<>();
    entities.add(nearPrey);
    entities.add(farPrey);

    animal.moveToNearestFood(entities, 1);
    // The predator should have moved closer to nearPrey
    assertTrue(animal.getPosition().subtract(firstVector).getMagnitude() < animal.getPosition().subtract(secondVector).getMagnitude());
    assertTrue(animal.getPosition().subtract(initialPosition).getMagnitude() > 1);
  }

  @Test
  void testMoveToEntity_NullEntity() {
    Vector initialPosition = animal.getPosition();
    animal.moveToEntity(null, 1.0);
    assertEquals(initialPosition, animal.getPosition(), "Position should not change when entity is null.");
  }

  @Test
  void testMoveToEntity_SamePosition() {
    Vector initialPosition = animal.getPosition();
    // Create a prey at the same position
    Prey prey = new Prey(data.getPreysData()[0].generateRandomGenetics(), initialPosition);
    animal.moveToEntity(prey, 1.0);
    assertEquals(initialPosition, animal.getPosition(), "Position should remain the same when target is at same location.");
  }

  @Test
  void testMoveToEntity_ValidEntity() {
    Vector initialPosition = animal.getPosition();
    // Create a prey at a slightly different position
    Prey prey = new Prey(data.getPreysData()[0].generateRandomGenetics(), initialPosition.add(new Vector(10, 10)));
    animal.moveToEntity(prey, 1.0);
    // The animal should have moved closer to the prey
    assertNotEquals(initialPosition, animal.getPosition(), "Position should change when moving to a valid entity.");
    assertTrue(animal.getPosition().subtract(prey.getPosition()).getMagnitude() 
      < initialPosition.subtract(prey.getPosition()).getMagnitude(), "Animal should be closer to the prey than before.");
  }
}