package view;

import entities.generic.Entity;
import graphics.Display;
import simulation.Simulator;

import java.awt.*;
import java.util.ArrayList;

/**
 * Combines the Simulator and Display to visualise the simulation.
 * This is the "engine" that runs the entire simulation.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Engine {
  private final Display display; // The GUI display
  private final Simulator simulator; // The simulation
  private final Clock clock; // Clock to keep track of time
  private boolean running = false;

  private final static boolean DRAW_QUADTREE = false; // Draw the quadtree visualisation

  /**
   * 0 < scaleFactor < 1 => field is zoomed in
   * scaleFactor = 1 => field is screen size (1 field unit = 1px)
   * scale factor > 1 => field is zoomed out
   */
  private final double fieldScaleFactor; // Scales the field size up/down, so field size doesn't have to be screen size

    /**
   * Constructor - Create an engine to run the simulation.
   * @param displayWidth The width of the GUI display.
   * @param displayHeight The height of the GUI display.
   * @param fps FPS to run the simulation at.
   * @param fieldScaleFactor The scale factor of the field.
   */
  public Engine(int displayWidth, int displayHeight, int fps, double fieldScaleFactor) {
    this.fieldScaleFactor = fieldScaleFactor;
    int fieldWidth = (int) (displayWidth * fieldScaleFactor);
    int fieldHeight = (int) (displayHeight * fieldScaleFactor);

    simulator = new Simulator(fieldWidth, fieldHeight);
    display = new Display(displayWidth, displayHeight);
    clock = new Clock(fps);
  }

  /**
   * Main loop of the simulation.
   */
  private void run() {
    while (running) {
      simulator.step();

      display.fill(Color.BLACK);

      ArrayList<Entity> entities = simulator.getField().getAllEntities();
      // We draw the entities in order of oldest to youngest to prevent annoying overlap.
      for (int i = entities.size() - 1; i >= 0; i--) {
        Entity entity = entities.get(i);
        entity.draw(display, fieldScaleFactor);
      }


      if (DRAW_QUADTREE) { // Debug tool to show the quadtree.
        simulator.getField().getQuadtree().draw(display, fieldScaleFactor);
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
