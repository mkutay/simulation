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

  @Test
  void testSearchNearbyEntities() {
    List<Entity> entities = new ArrayList<>();
    Entity entity1 = new Prey(data.getPreysData()[0].generateRandomGenetics(), new Vector(50, 50 + genetics.getSight()));
    Entity entity2 = new Prey(data.getPreysData()[0].generateRandomGenetics(), new Vector(50, 50 + genetics.getSight() + 1));

    entities.add(entity1);
    entities.add(entity2);

    List<Entity> foundEntities = animal.searchNearbyEntities(entities, animal.genetics.getSight());

    assertEquals(1, foundEntities.size());
    assertTrue(foundEntities.contains(entity1));
    assertFalse(foundEntities.contains(entity2));
  }

  @Test
  void testSearchNearbyEntities_EmptyList() {
    List<Entity> entities = new ArrayList<>();
    List<Entity> foundEntities = animal.searchNearbyEntities(entities, animal.genetics.getSight());

    assertTrue(foundEntities.isEmpty());
  }

  @Test
  void testSearchNearbyEntities_OnlyItself() {
    List<Entity> entities = new ArrayList<>();
    entities.add(animal);

    List<Entity> foundEntities = animal.searchNearbyEntities(entities, animal.genetics.getSight());

    assertTrue(foundEntities.isEmpty());
  }

  @Test
  void testSearchNearbyEntities_Null() {
    List<Entity> entities = null;

    List<Entity> foundEntities = animal.searchNearbyEntities(entities, animal.genetics.getSight());

    assertTrue(foundEntities.isEmpty());
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
    double initialFoodLevel = animal.getFoodLevel();
    Vector initialPosition = animal.getPosition();

    Prey prey = new Prey(data.getPreysData()[0].generateRandomGenetics(), new Vector(55, 50));
    List<Entity> entities = new ArrayList<>();
    entities.add(prey);

    boolean movedToFood = animal.moveToNearestFood(entities, 1);
    assertTrue(movedToFood);
    assertTrue(animal.getFoodLevel() < initialFoodLevel); // Decrement due to chasing
    assertNotEquals(initialPosition.toString(), animal.getPosition().toString()); // Position should change
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
}