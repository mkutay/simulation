package entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import simulation.Field;
import simulation.simulationData.*;
import util.Vector;
import genetics.PlantGenetics;

class PlantTest {
  private PlantGenetics genetics;
  private Field field;

  @BeforeEach
  void setUp() {
    this.genetics = Data.getPlantsData()[0].generateRandomGenetics();
    this.field = new Field(100, 100);
  }

  @Test
  void testMultiply_ReturnsNonEmptyListWhenAllowed() {
    PlantGenetics maxSpawnRateGenetics = new PlantGenetics(
      genetics.getMaxAge(),
      genetics.getMatureAge(),
      1.0d,
      genetics.getSize(),
      genetics.getName(),
      genetics.getColour(),
      genetics.getNumberOfSeeds(),
      genetics.getMaxOffspringSpawnDistance(),
      genetics.getOvercrowdingThreshold(),
      genetics.getOvercrowdingRadius(),
      genetics.getMutationRate()
    );
    Plant plant = new Plant(maxSpawnRateGenetics, new Vector(50, 50));
    plant.setAge(maxSpawnRateGenetics.getMatureAge() + 1);
    List<Plant> offspring = plant.multiply(field);
    assertFalse(offspring.isEmpty(), "Expected non-empty offspring list when plant can multiply.");
    assertEquals(genetics.getNumberOfSeeds(), offspring.size(), "Offspring should match genetics seed count.");
  }

  @Test
  void testMultiply_ReturnsEmptyListWhenRandomFails() {
    PlantGenetics minSpawnRateGenetics = new PlantGenetics(
      genetics.getMaxAge(),
      genetics.getMatureAge(),
      0d,
      genetics.getSize(),
      genetics.getName(),
      genetics.getColour(),
      genetics.getNumberOfSeeds(),
      genetics.getMaxOffspringSpawnDistance(),
      genetics.getOvercrowdingThreshold(),
      genetics.getOvercrowdingRadius(),
      genetics.getMutationRate()
    );
    Plant plant = new Plant(minSpawnRateGenetics, new Vector(50, 50));
    plant.setAge(minSpawnRateGenetics.getMatureAge() + 1);
    List<Plant> offspring = plant.multiply(field);
    assertTrue(offspring.isEmpty(), "Expected empty offspring list when random chance fails.");
  }

  @Test
  void testMultiply_ReturnsEmptyListWhenCannotMultiply() {
    Plant plant = new Plant(genetics, new Vector(50, 50)) {
      @Override
      protected boolean canMultiply() {
        return false;
      }
    };
    List<Plant> offspring = plant.multiply(field);
    assertTrue(offspring.isEmpty(), "Expected empty list when canMultiply() is false.");
  }

  @Test
  void testMultiply_ReturnsEmptyListWhenZeroSeeds() {
    PlantGenetics zeroSeedGenetics = new PlantGenetics(
      genetics.getMaxAge(),
      genetics.getMatureAge(),
      1.0d,
      genetics.getSize(),
      genetics.getName(),
      genetics.getColour(),
      0,
      genetics.getMaxOffspringSpawnDistance(),
      genetics.getOvercrowdingThreshold(),
      genetics.getOvercrowdingRadius(),
      genetics.getMutationRate()
    );
    Plant plant = new Plant(zeroSeedGenetics, new Vector(50, 50));
    plant.setAge(zeroSeedGenetics.getMatureAge() + 1);
    List<Plant> offspring = plant.multiply(field);
    assertTrue(offspring.isEmpty(), "Expected empty offspring list when number of seeds is zero.");
  }

  @Test
  void testMultiply_ReturnsEmptyListWhenBeyondMaxAge() {
    PlantGenetics maxSpawnRateGenetics = new PlantGenetics(
      genetics.getMaxAge(),
      genetics.getMatureAge(),
      1.0d,
      genetics.getSize(),
      genetics.getName(),
      genetics.getColour(),
      genetics.getNumberOfSeeds(),
      genetics.getMaxOffspringSpawnDistance(),
      genetics.getOvercrowdingThreshold(),
      genetics.getOvercrowdingRadius(),
      genetics.getMutationRate()
    );
    Plant plant = new Plant(maxSpawnRateGenetics, new Vector(50, 50));
    plant.setAge(genetics.getMaxAge() + 1);
    List<Plant> offspring = plant.multiply(field);
    assertTrue(offspring.isEmpty(), "Expected empty list when plant is beyond max age.");
  }

  @Test
  void testMultiply_OffspringWithinSpawnDistance() {
    PlantGenetics spawnDistGenetics = new PlantGenetics(
      genetics.getMaxAge(),
      genetics.getMatureAge(),
      1.0d,
      genetics.getSize(),
      genetics.getName(),
      genetics.getColour(),
      3,
      10.0d,
      genetics.getOvercrowdingThreshold(),
      genetics.getOvercrowdingRadius(),
      genetics.getMutationRate()
    );
    Plant plant = new Plant(spawnDistGenetics, new Vector(50, 50));
    plant.setAge(spawnDistGenetics.getMatureAge() + 1);
    List<Plant> offspring = plant.multiply(field);
    assertEquals(3, offspring.size(), "Should create 3 offspring when allowed.");
    for (Plant child : offspring) {
      double distance = plant.getPosition().subtract(child.getPosition()).getMagnitude();
      assertTrue(distance <= spawnDistGenetics.getMaxOffspringSpawnDistance(), "Offspring is planted too far from parent.");
    }
  }
}