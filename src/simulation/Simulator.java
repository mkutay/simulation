package simulation;

import entities.Prey;
import entities.Predator;
import entities.Plant;
import simulation.simulationData.Data;
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
    Data simulationData = new Data();
    field = new FieldBuilder(width, height, simulationData).build();
    simulatorView = new SimulatorView();
  }

  public void simulate(int numSteps) {
    for (int n = 1; n <= numSteps && field.isViable(); n++) {
      simulateOneStep();
    }
  }

  //TODO re-implement movement and add breeding system + eating/hunger system
  public void simulateOneStep() {
    step++;

    System.out.println("Step " + step);

    for (Plant plant : field.getPlants()) {
      //plant.update();
      System.out.println(plant);
      plant.multiply();
      plant.incrementAge();
    }

    for (Prey prey : field.getPreys()) {
      System.out.println(prey);
      //prey.update();
      prey.incrementAge();
      field.putInBounds(prey);
    }

    for (Predator predator : field.getPredators()) {
      System.out.println(predator);
      //predator.update();
      predator.incrementAge();
      field.putInBounds(predator);
    }

    field.removeDeadEntities();

    simulatorView.updateView();
  }
}
