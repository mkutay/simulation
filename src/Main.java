import api.Connector;
import simulation.simulationData.Data;
import util.Parser;
import view.Engine;

/**
 * Main class to start the simulation through the Engine class.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Main {
  private static final String PATH = System.getProperty("user.dir"); // The main directory of the project.

  public static void main(String[] args) {
    Connector.getInstance().start(); // For use with the web API.

    // If you want to run the simulation without the web API, uncomment the following lines:
    // Data.setSimulationData(Parser.parseSimulationData(Parser.getContentsOfFile(PATH + "/src/simulation_data.json")));
    // new Engine(800, 600, 60).start();
  }
}
