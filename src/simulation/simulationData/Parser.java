package simulation.simulationData;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

/**
 * A class to parse JSON data from files. It can get the contents of a file as a String,
 * and parse the contents as AnimalData or PlantData.
 */
public class Parser {
  /**
   * Serialise animal data from JSON content.
   * @param jsonContent The JSON contents.
   * @return The animal data parsed, serialised as AnimalData.
   */
  public static AnimalData[] parseAnimalJson(String jsonContent) {
    Gson g = new Gson();
    return g.fromJson(jsonContent, AnimalData[].class);
  }

  /**
   * Serialise plant data from JSON content.
   * @param jsonContent The JSON contents.
   * @return The plant data parsed, serialised as PlantData.
   */
  public static PlantData[] parsePlantJson(String jsonContent) {
    Gson g = new Gson();
    return g.fromJson(jsonContent, PlantData[].class);
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
