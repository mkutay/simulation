package view;

import entities.generic.Entity;
import graphics.Display;
import simulation.Simulator;
import simulation.simulationData.Data;

import java.awt.*;
import java.util.List;
import java.util.HashMap;

/**
 * Combines the Simulator and Display to visualise the simulation.
 * This is the "engine" that runs the entire simulation.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Engine {
  private final Display display; // The GUI display.
  private final Simulator simulator; // The simulation.
  private final Clock clock; // Clock to keep track of time.
  private boolean running = false; // Whether the simulation is running.

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
   */
  public Engine(int displayWidth, int displayHeight, int fps) {
    fieldScaleFactor = Data.getFieldScaleFactor();
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

      List<Entity> entities = simulator.getField().getAllEntities();
      // We draw the entities in order of oldest to youngest to prevent annoying overlap.
      for (int i = entities.size() - 1; i >= 0; i--) {
        Entity entity = entities.get(i);
        entity.draw(display, fieldScaleFactor);
      }

      // Update weather effects.
      if (Data.getDoWeatherCycle()) {
        simulator.getField().environment.drawWeatherEffects(display);
      }

      // Draw the darkning screen effect before any text to not obscure them:
      if (Data.getDoDayNightCycle()) {
        simulator.getField().environment.drawDarknessEffect(display);
      }

      // Debug tool to show the quadtree. It also looks really cool!
      if (Data.getShowQuadTrees()) {
        simulator.getField().getQuadtree().draw(display, fieldScaleFactor);
      }

      // Update weather text.
      if (Data.getDoWeatherCycle()) {
        simulator.getField().environment.drawWeatherText(display);
      }

      // Update time text.
      if (Data.getDoDayNightCycle()) {
        simulator.getField().environment.drawTimeText(display);
      }

      drawFieldDataText();

      display.update();
      clock.tick();
    }
  }

  /**
   * Lists all alive entities and the number of existing entities for each species
   * in the bottom left corner.
   */
  private void drawFieldDataText() {
    HashMap<String, Integer> fieldData = simulator.getFieldData();
    int fontSize = 15;
    int startY = (display.getHeight() - fieldData.size() * fontSize);
    int i = 0;
    for (String entityName : fieldData.keySet()) {
      String data = entityName + ": " + fieldData.get(entityName);
      display.drawText(data, fontSize, 5, startY + i * fontSize, Color.WHITE);
      i++;
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
