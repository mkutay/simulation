import java.util.ArrayList;
import java.util.List;

import entities.Location;
import entities.Prey;
import entities.Vector;
import entities.Predator;
import entities.Plant;
import genetics.AnimalData;
import genetics.PlantData;

public class Simulator {
  public static final String PATH = "/Users/kutay/code/we-get-these-100s"; // Change this to the path of the project -- this is temporary
  
  private int step;
  private SimulatorView simulatorView;
  private Field field;

  private AnimalData[] preysData; // An array of prey species data
  private AnimalData[] predatorsData; // An array of predator species data
  private PlantData[] plantsData; // An array of plant types data

  public Simulator() {
    this(50, 50);
  }

  public Simulator(int width, int height) {
    step = 0;

    preysData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/prey_data.json"));
    predatorsData = Parser.parseAnimalJson(Parser.getContentsOfFile(PATH + "/predator_data.json"));
    plantsData = Parser.parsePlantJson(Parser.getContentsOfFile(PATH + "/plant_data.json"));

    field = new Field(width, height, preysData, predatorsData, plantsData);
    simulatorView = new SimulatorView();
  }

  public void runLongSimulation() {
    simulate(700);
  }

  public void simulate(int numSteps) {
    for (int n = 1; n <= numSteps && field.isViable(); n++) {
      simulateOneStep();
    }
  }

  public void simulateOneStep() {
    step++;

    for (Prey prey : field.getPreys()) {
      Location preyLocation = prey.getLocation();
      List<Predator> seenPredators = field.seeingPredators(prey);
      List<Plant> seenPlants = field.seeingPlants(prey);

      List<Vector> vectors = new ArrayList<>();

      seenPlants.forEach(seenPlant -> vectors.add(Vector.createFromLocations(preyLocation, seenPlant.getLocation())));
      seenPredators.forEach(seenPredator -> vectors.add(Vector.createFromLocations(seenPredator.getLocation(), preyLocation)));

      prey.move(Vector.addVectors(vectors));
    }

    for (Predator predator : field.getPredators()) {
      Location predatorLocation = predator.getLocation();
      List<Prey> seenPreys = field.seeingPreys(predator);

      List<Vector> vectors = new ArrayList<>();
      seenPreys.forEach(seenPrey -> vectors.add(Vector.createFromLocations(predatorLocation, seenPrey.getLocation())));

      predator.move(Vector.addVectors(vectors));
    }

    simulatorView.updateView();
  }
}
