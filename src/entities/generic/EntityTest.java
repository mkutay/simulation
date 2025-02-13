package entities.generic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.*;
import genetics.AnimalGenetics;
import simulation.simulationData.Data;
import util.Vector;

class EntityTest {
  private Animal animal;
  private AnimalGenetics genetics;

  @BeforeEach
  void setUp() {
    this.genetics = Data.getPredatorsData()[0].generateRandomGenetics();
    this.animal = new Predator(genetics, new Vector(50, 50));
  }

  @Test
  void testIsColliding() {
    AnimalGenetics genetics1 = Data.getPreysData()[0].generateRandomGenetics();
    AnimalGenetics genetics2 = Data.getPreysData()[0].generateRandomGenetics();
    AnimalGenetics genetics3 = Data.getPreysData()[0].generateRandomGenetics();
    AnimalGenetics genetics4 = Data.getPreysData()[0].generateRandomGenetics();
    Entity entity1 = new Prey(genetics1, new Vector(50, 50));
    Entity entity2 = new Prey(genetics2, new Vector(50, 50 + entity1.getSize()));
    Entity entity3 = new Prey(genetics3, new Vector(50, 50 + genetics1.getSize() + genetics3.getSize()));
    Entity entity4 = new Prey(genetics4, new Vector(50, 50 + genetics1.getSize() + genetics4.getSize() + 1));
    assertTrue(entity1.isColliding(entity2));
    assertTrue(entity1.isColliding(entity3));
    assertFalse(entity1.isColliding(entity4));
  }

  @Test
  void testIsColliding_Null() {
    AnimalGenetics genetics = Data.getPreysData()[0].generateRandomGenetics();
    Entity entity = new Prey(genetics, new Vector(50, 50));
    assertFalse(entity.isColliding(null));
  }

  @Test
  void testSearchNearbyEntities() {
    List<Entity> entities = new ArrayList<>();
    Entity entity1 = new Prey(Data.getPreysData()[0].generateRandomGenetics(), new Vector(50, 50 + genetics.getSight()));
    Entity entity2 = new Prey(Data.getPreysData()[0].generateRandomGenetics(), new Vector(50, 50 + genetics.getSight() + 1));

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

  @Test
  void testGetSameSpecies() {
    List<Entity> entities = new ArrayList<>();
    final int COUNT = 4;
    for (int i = 0; i < COUNT; i++) {
      entities.add(new Predator(Data.getPredatorsData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    }
    Predator deadPredator = new Predator(Data.getPredatorsData()[0].generateRandomGenetics(), Vector.getRandomVector());
    deadPredator.setDead();
    entities.add(new Prey(Data.getPreysData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Plant(Data.getPlantsData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Prey(Data.getPreysData()[1].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Prey(Data.getPreysData()[1].generateRandomGenetics(), Vector.getRandomVector()));
    List<Entity> sameEntities = animal.getSameSpecies(entities);
    assertTrue(sameEntities.size() == COUNT);
    for (int i = 0; i < COUNT; i++) {
      assertEquals(sameEntities.get(i).getName(), animal.getName());
    }
  }

  @Test
  void testGetSameSpecies_WithPlant() {
    Plant plant = new Plant(Data.getPlantsData()[0].generateRandomGenetics(), Vector.getRandomVector());
    List<Entity> entities = new ArrayList<>();
    final int COUNT = 4;
    for (int i = 0; i < COUNT; i++) {
      entities.add(new Plant(Data.getPlantsData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    }
    Plant deadPlant = new Plant(Data.getPlantsData()[0].generateRandomGenetics(), Vector.getRandomVector());
    deadPlant.setDead();
    entities.add(new Prey(Data.getPreysData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Predator(Data.getPredatorsData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Prey(Data.getPreysData()[1].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Prey(Data.getPreysData()[1].generateRandomGenetics(), Vector.getRandomVector()));
    List<Entity> sameEntities = plant.getSameSpecies(entities);
    assertTrue(sameEntities.size() == COUNT);
    for (int i = 0; i < COUNT; i++) {
      assertEquals(sameEntities.get(i).getName(), plant.getName());
    }
  }

  @Test
  void testGetSameSpecies_WithPrey() {
    Prey prey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), Vector.getRandomVector());
    List<Entity> entities = new ArrayList<>();
    final int COUNT = 4;
    for (int i = 0; i < COUNT; i++) {
      entities.add(new Prey(Data.getPreysData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    }
    Prey deadPrey = new Prey(Data.getPreysData()[0].generateRandomGenetics(), Vector.getRandomVector());
    deadPrey.setDead();
    entities.add(new Predator(Data.getPredatorsData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Predator(Data.getPredatorsData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Plant(Data.getPlantsData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Plant(Data.getPlantsData()[0].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Prey(Data.getPreysData()[1].generateRandomGenetics(), Vector.getRandomVector()));
    entities.add(new Prey(Data.getPreysData()[1].generateRandomGenetics(), Vector.getRandomVector()));
    List<Entity> sameEntities = prey.getSameSpecies(entities);
    assertTrue(sameEntities.size() == COUNT);
    for (int i = 0; i < COUNT; i++) {
      assertEquals(sameEntities.get(i).getName(), prey.getName());
    }
  }
}