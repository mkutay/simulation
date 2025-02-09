import view.Engine;

public class Main {
  public static void main(String[] args) {
    int fps = 60;
    double fieldScaleFactor = 0.6;
    Engine engine = new Engine(200, 200, fps, fieldScaleFactor);
    engine.start();
  }
}
