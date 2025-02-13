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
import genetics.Gender;

class AnimalTest {
  private Animal animal;
  private AnimalGenetics genetics;
  private Field field;

  @BeforeEach
  void setUp() {
    this.genetics = Data.getPredatorsData()[0].generateRandomGenetics();
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
    Prey prey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), animal.getPosition());
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
    Prey prey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), animal.getPosition().add(new Vector(genetics.getSize(), 0)));
    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(prey);
  
    animal.eat(nearbyEntities);
  
    assertFalse(prey.isAlive());
    assertTrue(animal.getFoodLevel() > initialFoodLevel);
  }
  
  @Test
  void testEat_ShouldntEatWhatItCannotEat() {
    Entity plant = new Plant(Data.getPlantsData()[0].generateRandomGenetics(), new Vector(50, 50));
    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(plant);
  
    animal.eat(nearbyEntities);
  
    assertTrue(plant.isAlive());
  }
  
  @Test
  void testEat_NotColliding() {
    Prey prey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), animal.getPosition().add(new Vector(genetics.getSight() + 1, 0)));
    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(prey);
  
    animal.eat(nearbyEntities);
  
    assertTrue(prey.isAlive());
  }
  
  @Test
  void testEat_EntityAlreadyDead() {
    double initialFoodLevel = animal.getFoodLevel();
    Prey prey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), animal.getPosition());
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

    Prey prey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), new Vector(55, 50));
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
    Prey nearPrey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), firstVector);
    Prey farPrey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), secondVector);
    List<Entity> entities = new ArrayList<>();
    entities.add(nearPrey);
    entities.add(farPrey);

    animal.moveToNearestFood(entities, 1);
    // The predator should have moved closer to nearPrey
    assertTrue(animal.getPosition().subtract(firstVector).getMagnitude() < animal.getPosition().subtract(secondVector).getMagnitude());
    assertTrue(animal.getPosition().subtract(initialPosition).getMagnitude() > 1);
  }
  
  @Test
  void testMoveToNearestMate_NoMateNearby() {
    Vector initialPosition = animal.getPosition();
    boolean movedToMate = animal.moveToNearestMate(new ArrayList<>(), 1.0);
    assertFalse(movedToMate);
    assertEquals(initialPosition, animal.getPosition());
  }
  
  @Test
  void testMoveToNearestMate_FoundSingleMate() {
    AnimalGenetics mateGenetics = new AnimalGenetics(
      genetics.getMultiplyingRate(),
      genetics.getMaxLitterSize(),
      genetics.getMaxAge(),
      genetics.getMatureAge(),
      genetics.getMutationRate(),
      genetics.getMaxSpeed(),
      genetics.getSight(),
      genetics.getGender() == Gender.MALE ? Gender.FEMALE : Gender.MALE,
      genetics.getSize(),
      genetics.getEats(),
      genetics.getName(),
      genetics.getColour(),
      genetics.getOvercrowdingThreshold(),
      genetics.getOvercrowdingRadius(),
      genetics.getMaxOffspringSpawnDistance()
    );
    Vector initialPosition = animal.getPosition();
    Predator potentialMate = new Predator(mateGenetics, initialPosition.add(new Vector(5, 0)));

    // Set the age of the animals to be mature
    animal.setAge(genetics.getMatureAge() + 1);
    potentialMate.setAge(mateGenetics.getMatureAge() + 1);

    // Give them food, so that they can multiply
    List<Entity> preys = new ArrayList<>();
    preys.add(new Prey(Data.getPreysData()[0].generateRandomGenetics(), initialPosition));
    preys.add(new Prey(Data.getPreysData()[0].generateRandomGenetics(), initialPosition));
    animal.eat(preys);
    preys.removeFirst();
    preys.removeFirst();
    preys.add(new Prey(Data.getPreysData()[0].generateRandomGenetics(), potentialMate.getPosition()));
    preys.add(new Prey(Data.getPreysData()[0].generateRandomGenetics(), potentialMate.getPosition()));
    potentialMate.eat(preys);

    List<Entity> nearbyEntities = new ArrayList<>();
    nearbyEntities.add(potentialMate);
  
    boolean movedToMate = animal.moveToNearestMate(nearbyEntities, 1.0);
    assertTrue(movedToMate);
    assertNotEquals(animal.getPosition(), initialPosition);
    assertTrue(animal.getPosition().subtract(potentialMate.getPosition()).getMagnitude()
      < initialPosition.subtract(potentialMate.getPosition()).getMagnitude());
  }
}