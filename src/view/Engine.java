package view;

import entities.generic.Entity;
import graphics.Display;
import simulation.environment.Environment;
import simulation.Simulator;
import simulation.environment.Weather;
import simulation.simulationData.Data;

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
  private boolean running = false; // Whether the simulation is running

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

      ArrayList<Entity> entities = simulator.getField().getAllEntities();
      // We draw the entities in order of oldest to youngest to prevent annoying overlap.
      for (int i = entities.size() - 1; i >= 0; i--) {
        Entity entity = entities.get(i);
        entity.draw(display, fieldScaleFactor);
      }


      if (Data.getShowQuadTrees()) { // Debug tool to show the quadtree.
        simulator.getField().getQuadtree().draw(display, fieldScaleFactor);
      }

      if (Data.getDoDayNightCycle()) {
        drawTimeText();
      }

      if (Data.getDoWeatherCycle()) {
        drawWeatherText();
        updateWeatherEffects(display);
      }

      display.update();
      clock.tick();
    }
  }

  private void updateWeatherEffects(Display display) {
    Environment environment = simulator.getField().environment;
    environment.spawnRain(display);
    environment.spawnLightning(display);

    environment.updateWeatherEffects(display);
  }



  private void drawWeatherText() {
    Weather weather = simulator.getField().environment.getWeather();
    display.drawText(weather.toString(), 20, 5, 40, Color.WHITE);
    if (weather == Weather.WINDY || weather == Weather.STORM) {
      display.drawText("Wind Direction :", 20, 5, 60, Color.WHITE);
      double windDirection = simulator.getField().environment.getWindDirection();
      display.drawCircle(210, 55, 20, Color.BLACK);
      display.drawArrow(210, 55, windDirection, 20, Color.WHITE);
    }
  }

  /**
   * Renders text of the current time of day if the day/night cycle mode is enabled
   */
  private void drawTimeText() {
    String time = simulator.getField().environment.getTimeFormatted();
    display.drawText(time, 20, 5, 20, Color.WHITE);
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
