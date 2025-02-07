package entities.generic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.*;
import genetics.AnimalGenetics;
import simulation.simulationData.*;
import util.Vector;

class EntityTest {
  private Data data;

  @BeforeEach
  void setUp() {
    this.data = new Data();
  }

  @Test
  void testIsColliding() {
    AnimalGenetics genetics1 = data.getPreysData()[0].generateRandomGenetics();
    AnimalGenetics genetics2 = data.getPreysData()[0].generateRandomGenetics();
    AnimalGenetics genetics3 = data.getPreysData()[0].generateRandomGenetics();
    AnimalGenetics genetics4 = data.getPreysData()[0].generateRandomGenetics();
    Entity entity1 = new Prey(genetics1, new Vector(50, 50));
    Entity entity2 = new Prey(genetics2, new Vector(50, 50 + entity1.getSize()));
    Entity entity3 = new Prey(genetics3, new Vector(50, 50 + genetics1.getSize() + genetics3.getSize()));
    Entity entity4 = new Prey(genetics4, new Vector(50, 50 + genetics1.getSize() + genetics4.getSize() + 1));
    assertTrue(Entity.isColliding(entity1, entity2));
    assertTrue(Entity.isColliding(entity1, entity3));
    assertFalse(Entity.isColliding(entity1, entity4));
  }

  @Test
  void testIsColliding_Null() {
    AnimalGenetics genetics = data.getPreysData()[0].generateRandomGenetics();
    Entity entity = new Prey(genetics, new Vector(50, 50));
    assertFalse(Entity.isColliding(entity, null));
    assertFalse(Entity.isColliding(null, entity));
    assertFalse(Entity.isColliding(null, null));
  }
}