import java.util.List;

import entities.Plant;
import entities.Predator;
import entities.Prey;
import genetics.AnimalGeneticsInterval;
import genetics.PlantGeneticsInterval;

public class Field {
  private int width;
  private int height;
  private List<Prey> preys;
  private List<Predator> predators;
  private List<Plant> plants;

  public Field(int width, int height) {
    this.width = width;
    this.height = height;
    // The file paths are hardcoded for now
    AnimalGeneticsInterval[] preysInterval = Parser.parseAnimalJson(Parser.getContentsOfFile("/workspaces/junit-testing/prey_data.json"));
    AnimalGeneticsInterval[] predatorsInterval = Parser.parseAnimalJson(Parser.getContentsOfFile("/workspaces/junit-testing/predator_data.json"));
    PlantGeneticsInterval[] plantsInterval = Parser.parsePlantJson(Parser.getContentsOfFile("/workspaces/junit-testing/plant_data.json"));
    System.out.println(preysInterval[0].name);
    System.out.println(preysInterval[0].maxAge[0]);
  }
}
