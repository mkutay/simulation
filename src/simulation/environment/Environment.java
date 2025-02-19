package simulation.environment;

import graphics.Display;
import util.Vector;

/**
 * Stores the weather and time controllers and delegates the updating of the
 * environment to them.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Environment {
  private WeatherController weatherController; // The weather controller.
  private TimeController timeController; // The time controller.

  /**
   * Constructor -- Creates a new environment with weather and time controllers.
   */
  public Environment() {
    weatherController = new WeatherController();
    timeController = new TimeController();
  }

  /**
   * Update the weather effects on the display.
   * @param display The display to update the weather effects on.
   */
  public void drawWeatherEffects(Display display) {
    weatherController.drawWeatherEffects(display);
  }

  /**
   * Update the weather that affects the simulation.
   */
  public void updateWeather() {
    weatherController.updateWeather();
  }

  public void drawWeatherText(Display display) {
    weatherController.drawWeatherText(display);
  }

  /**
   * Update the time of day, and if the day has changed, change the weather.
   * @param dayNightCycleRate The amount of time to pass per update.
   */
  public void updateTime(double dayNightCycleRate) {
    boolean dayChanged = timeController.incrementTime(dayNightCycleRate);
    if (dayChanged) {
      weatherController.changeWeather();
    }
  }

  /**
   * Draw the darkness effect onto the display.
   * @param display The display to draw the effects on.
   */
  public void drawDarknessEffect(Display display) {
    timeController.drawDarknessEffect(display);
  }

  /**
   * Update the time effects on the display; mainly time text.
   * @param display The display to update the time effects on.
   */
  public void drawTimeText(Display display) {
    timeController.drawTimeText(display);
  }

  /**
   * @return The time with some additional information to be displayed to the screen.
   */
  public String getTimeFormatted() {
    return timeController.getTimeFormatted();
  }

  // Getters:
  public Weather getWeather() { return weatherController.getWeather(); }
  public double getWindDirection() { return weatherController.getWindDirection(); }
  public Vector getWindVector() { return weatherController.getWindVector(); }
  public boolean isDay() { return timeController.isDay(); }
}