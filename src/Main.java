import view.Engine;

/**
 * Main class to start the simulation through the Engine class.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class Main {
  public static void main(String[] args) {
    int fps = 60;
    int width = 600;
    int height = 600;
    Engine engine = new Engine(width, height, fps);
    engine.start();
  }
}
