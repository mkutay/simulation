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
  public static void main(String[] args) throws Exception {
    Connector.getInstance().start(); // For use with the web API.

    // If you want to run the simulation without the web API, uncomment the following lines
    // and change the constructor of Display to use RenderPanelGUI
    // Data.setSimulationData(Parser.parseSimulationData(Parser.getContentsOfFile(System.getProperty("user.dir") + "/simulation_data.json")));
    // new Engine(800, 600, 60, "").start();
  }
}
