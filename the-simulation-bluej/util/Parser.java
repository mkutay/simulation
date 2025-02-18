package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

import simulation.simulationData.SimulationData;

/**
 * A class to parse JSON data from files. It can get the contents of a file as a String,
 * and parse the contents as either AnimalData or PlantData.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Parser {
  /**
   * Serialise simulation data from some JSON content.
   * @param jsonContent The JSON content.
   * @return The simulation data parsed, serialised as the SimulationData class.
   */
  public static SimulationData parseSimulationData(String jsonContent) {
    Gson g = new Gson();
    return g.fromJson(jsonContent, SimulationData.class);
  }

  /**
   * Get the contents of a file as a String.
   * @param fileName The name of the file.
   * @return The contents of the file.
   */
  public static String getContentsOfFile(String fileName) {
    try {
      return new String(Files.readAllBytes(Paths.get(fileName)));
    } catch (IOException e) {
      System.out.println("Error reading file: " + fileName);
      e.printStackTrace();
      return null;
    }
  }
}
