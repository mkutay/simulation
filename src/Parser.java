import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

import genetics.AnimalData;
import genetics.PlantData;

public class Parser {
  /**
   * Parse a GeneticsInterval from a file
   * @param fileName The name of the file to parse
   * @return The GeneticsInterval parsed from the file
   */
  public static AnimalData[] parseAnimalJson(String jsonContent) {
    Gson g = new Gson();
    return g.fromJson(jsonContent, AnimalData[].class);
  }

  public static PlantData[] parsePlantJson(String jsonContent) {
    Gson g = new Gson();
    return g.fromJson(jsonContent, PlantData[].class);
  }

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
