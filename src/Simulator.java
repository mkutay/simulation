public class Simulator {
  private SimulatorView simulatorView;
  private Field field;

  public Simulator() {
    this(50, 50);
  }

  public Simulator(int width, int height) {
    field = new Field(width, height);
    simulatorView = new SimulatorView();
  }
}
