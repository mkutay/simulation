package entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import simulation.Field;
import simulation.simulationData.*;
import util.Parser;
import util.Vector;
import genetics.PlantGenetics;

/**
 * Test class for the Plant entity. Tests methods underneath it.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
class PlantTest {
  private PlantGenetics genetics;
  private Field field;

  @BeforeEach
  void setUp() {
    final String PATH = System.getProperty("user.dir");
    Data.setSimulationData(Parser.parseSimulationData(Parser.getContentsOfFile(PATH + "/src/simulation_data.json")));
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
      genetics.getMutationRate(),
      genetics.getRainingGrowthFactor()
    );
    Plant plant = new Plant(maxSpawnRateGenetics, new Vector(50, 50));
    plant.setAge(maxSpawnRateGenetics.getMatureAge() + 1);
    List<Plant> offspring = plant.multiply(field);
    assertFalse(offspring.isEmpty(), "Expected non-empty offspring list when plant can multiply.");
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
      genetics.getMutationRate(),
      genetics.getRainingGrowthFactor()
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
      genetics.getMutationRate(),
      genetics.getRainingGrowthFactor()
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
      genetics.getMutationRate(),
      genetics.getRainingGrowthFactor()
    );
    Plant plant = new Plant(maxSpawnRateGenetics, new Vector(50, 50));
    plant.setAge(genetics.getMaxAge() + 1);
    List<Plant> offspring = plant.multiply(field);
    assertTrue(offspring.isEmpty(), "Expected empty list when plant is beyond max age.");
  }
}