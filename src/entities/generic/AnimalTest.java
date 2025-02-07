package entities.generic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import entities.*;
import simulation.simulationData.*;
import util.Vector;
import genetics.AnimalGenetics;

class AnimalTest {
  private Animal animal;
  private AnimalGenetics genetics;
  private Data data;

  @BeforeEach
  void setUp() {
    this.data = new Data();
    this.genetics = data.getPredatorsData()[0].generateRandomGenetics();
    this.animal = new Predator(genetics, new Vector(50, 50));
  }

  @Test
  void testSearchNearbyEntities() {
    ArrayList<Entity> entities = new ArrayList<>();
    Entity entity1 = new Prey(data.getPreysData()[0].generateRandomGenetics(), new Vector(50, 50 + genetics.getSight()));
    Entity entity2 = new Prey(data.getPreysData()[0].generateRandomGenetics(), new Vector(50, 50 + genetics.getSight() + 1));

    entities.add(entity1);
    entities.add(entity2);

    ArrayList<Entity> foundEntities = animal.searchNearbyEntities(entities);

    assertEquals(1, foundEntities.size());
    assertTrue(foundEntities.contains(entity1));
    assertFalse(foundEntities.contains(entity2));
  }

  @Test
  void testSearchNearbyEntities_EmptyList() {
    ArrayList<Entity> entities = new ArrayList<>();
    ArrayList<Entity> foundEntities = animal.searchNearbyEntities(entities);

    assertTrue(foundEntities.isEmpty());
  }

  @Test
  void testSearchNearbyEntities_OnlyItself() {
    ArrayList<Entity> entities = new ArrayList<>();
    entities.add(animal);

    ArrayList<Entity> foundEntities = animal.searchNearbyEntities(entities);

    assertTrue(foundEntities.isEmpty());
  }

  @Test
  void testSearchNearbyEntities_Null() {
    ArrayList<Entity> entities = null;

    ArrayList<Entity> foundEntities = animal.searchNearbyEntities(entities);

    assertTrue(foundEntities.isEmpty());
  }
}