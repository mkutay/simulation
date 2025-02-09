package view;

import entities.generic.Entity;
import graphics.Display;
import simulation.Simulator;

import java.awt.*;

/**
 * Combines the Simulator and Display to visualise the simulation.
 */
public class Engine {
  private final Display display;
  private final Simulator simulator;
  private final Clock clock;

  private boolean running = false;

  /**
   * 0 < scaleFactor < 1 => field is zoomed in
   * scaleFactor = 1 => field is screen size (1 field unit = 1px)
   * scale factor > 1 => field is zoomed out
   */
  private final double fieldScaleFactor; // Scales the field size up/down, so field size doesn't have to be screen size
  private final int fps;

  /**
   * Create an engine to run the simulation.
   * @param displayWidth the width of the GUI display.
   * @param displayHeight the height of the GUI display.
   * @param fps FPS to run simulation at.
   */
  public Engine(int displayWidth, int displayHeight, int fps, double fieldScaleFactor) {
    this.fps = fps;
    this.fieldScaleFactor = fieldScaleFactor;
    int fieldWidth = (int) (displayWidth * fieldScaleFactor);
    int fieldHeight = (int) (displayHeight * fieldScaleFactor);

    simulator = new Simulator(fieldWidth, fieldHeight);
    display = new Display(displayWidth, displayHeight);
    clock = new Clock(this.fps);
  }

  /**
   * Initialise Engine with FPS set to 60
   */
  public Engine(int displayWidth, int displayHeight, double fieldScaleFactor) {
    this(displayWidth, displayHeight, 60, fieldScaleFactor);
  }

  /**
   * Main loop of the simulation
   */
  private void run() {
    while (running) {
      simulator.step();

      display.fill(Color.BLACK);
      for (Entity entity : simulator.getEntities()) {
        entity.draw(display, fieldScaleFactor);
      }
      display.update();
      
      clock.tick();
    }
  }

  /**
   * Start simulation asynchronously on a new thread so that it can be
   * stopped in the main execution thread.
   */
  public void start() {
    running = true;
    Thread t = new Thread(this::run);
    t.start();
  }
}
