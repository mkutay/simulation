package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
  public static SimulationData parseSimulationData(String jsonContent) throws JsonSyntaxException {
    Gson g = new Gson();
    SimulationData simulationData = null;
    try {
      simulationData = g.fromJson(jsonContent, SimulationData.class);
    } catch (JsonSyntaxException e) {
      System.out.println("Error parsing JSON content: " + jsonContent);
      e.printStackTrace();
      throw e;
    }
    return simulationData;
  }

  /**
   * Get the contents of a file as a String.
   * @param fileName The name of the file.
   * @return The contents of the file.
   */
  public static String getContentsOfFile(String fileName) throws IOException {
    String contents = null;
    try {
      contents =  new String(Files.readAllBytes(Paths.get(fileName)));
    } catch (IOException e) {
      System.out.println("Error reading file: " + fileName);
      e.printStackTrace();
      throw e;
    }
    return contents;
  }

  public static SimulationData parseSimulationDataFromFile(String fileName) throws IOException, JsonSyntaxException {
    String contents = null;
    try {
      contents = getContentsOfFile(fileName);
    } catch (IOException e) {
      throw e;
    }
    SimulationData simulationData = null;
    try {
      simulationData = parseSimulationData(contents);
    } catch (JsonSyntaxException e) {
      throw e;
    }
    return simulationData;
  }
}
