package simulation;
import java.util.ArrayList;
import java.util.List;

import entities.Prey;
import entities.Vector;
import entities.Predator;
import entities.Plant;
import view.SimulatorView;

public class Simulator {  
  private int step;
  private final SimulatorView simulatorView;
  private final Field field;

  public Simulator() {
    this(20, 20);
  }

  public Simulator(int width, int height) {
    step = 0;
    field = new Field(width, height);
    simulatorView = new SimulatorView();
  }

  public void simulate(int numSteps) {
    for (int n = 1; n <= numSteps && field.isViable(); n++) {
      simulateOneStep();
    }
  }

  public void simulateOneStep() {
    step++;

    System.out.println("Step " + step);

    //TODO: Much of this code should be in the corresponding entity class! There should be an entity.update() method called here and not a huge deal else for now

    List<Plant> newPlants = new ArrayList<>(); // newly generated plants
    for (Plant plant : field.getPlants()) {
      System.out.println(plant);
      plant.multiply();
      plant.incrementAge();
    }
    
    List<Prey> newPreys = new ArrayList<>(); // newly born preys
    for (Prey prey : field.getPreys()) {
      System.out.println(prey);
      Vector preyLocation = prey.getPosition();
      List<Predator> seenPredators = field.seeingPredators(prey);
      List<Plant> seenPlants = field.seeingPlants(prey);

      List<Vector> vectors = new ArrayList<>();

      seenPlants.forEach(seenPlant -> vectors.add(seenPlant.getPosition().subtract(preyLocation)));// Vector.getVectorFromLocations(preyLocation, seenPlant.getPosition())));
      seenPredators.forEach(seenPredator -> vectors.add(seenPredator.getPosition().subtract(preyLocation)));

      prey.moveWithVector(Vector.addVectors(vectors));
      prey.incrementAge();
      prey.breed(field.seeingSameSpecies(prey));

      field.putInBounds(prey);
    }

    List<Predator> newPredators = new ArrayList<>(); // newly born predator
    for (Predator predator : field.getPredators()) {
      System.out.println(predator);
      Vector predatorLocation = predator.getPosition();
      List<Prey> seenPreys = field.seeingPreys(predator);

      List<Vector> vectors = new ArrayList<>();
      seenPreys.forEach(seenPrey -> vectors.add(seenPrey.getPosition().subtract(predatorLocation)));

      predator.moveWithVector(Vector.addVectors(vectors));
      predator.incrementAge();
      predator.breed(field.seeingSameSpecies(predator));

      field.putInBounds(predator);
    }

    field.removeDeadEntities();

    simulatorView.updateView();
  }
}
