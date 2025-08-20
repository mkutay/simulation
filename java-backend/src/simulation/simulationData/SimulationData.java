package simulation.simulationData;

public class SimulationData {
  public AnimalData[] preysData; // An array of prey species data.
  public AnimalData[] predatorsData; // An array of predator species data.
  public PlantData[] plantsData; // An array of plant types data.

  public double foodValueForAnimals; // Scales the food value of animals.
  public double foodValueForPlants; // Scales the food value of plants.
  public double animalHungerDrain; // Controls rate of foodLevel depletion over time.
  public double animalBreedingCost; // Scales how much food is consumed on breeding; use 0 for no food cost.

  public double mutationFactor; // The ratio that the genetics will mutate by.
  public double entityAgeRate; // Controls how fast the entities age.
  public double fieldScaleFactor; // The size of the field, smaller value means more zoomed in.
  public double weatherChangeProbability; // The probability of the weather changing.
  public double windStrength; // The strength of the wind in windy conditions.
  public double stormMovementSpeedFactor; // When a storm happens, the factor that hinders the entities' speed.
  public double dayNightCycleSpeed; // The speed of the day-night cycle -- how fast the time passes.
  public boolean doDayNightCycle; // Whether the day-night cycle is enabled.
  public boolean doWeatherCycle; // Whether the weather cycle is enabled.
  public boolean showQuadTrees; // Whether the quad trees are shown.

  public double animalHungerThreshold; // Animals will look for food at this threshold.
  public double animalDyingOfHungerThreshold; // Animals will prioritise looking for food at this threshold.
}
