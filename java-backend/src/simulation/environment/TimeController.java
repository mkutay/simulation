package simulation.environment;

import java.awt.Color;

import graphics.Display;

/**
 * Stores the weather and the time of day and updates it accordingly.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class TimeController {
  private int day = 1; // The current day
  private double timeOfDay = DAY_START; // Loops from 0 to 1. The current time of day.

  // Daytime is 8 AM to 8 PM:
  private final static double DAY_START = 1 / 3d; // Corresponds to 8 AM.
  private final static double DAY_END =  0.8d + 1 / 30d; // Corresponds to 8 PM.

  /**
   * Increments the time of day and the current day.
   * @param dayNightCycleRate The amount of time to pass per update.
   * @return True if the day has changed, false otherwise.
   */
  public boolean incrementTime(double dayNightCycleRate) {
    timeOfDay += dayNightCycleRate;
    if (timeOfDay >= 1) {
      timeOfDay = 0;
      day++;
      return true;
    }
    return false;
  }

  /**
   * Daytime is any time from 8 AM to 8 PM.
   * @return True if the time is day, false otherwise.
   */
  public boolean isDay() {
    return timeOfDay <= DAY_END && timeOfDay >= DAY_START;
  }

  /**
   * Converts the time of day value from 0-1 to a 24-hour clock time.
   * @return The time of day in the 24-hour format (HH:MM:SS) in a String.
   */
  private String get24HourTime() {
    double total_hours = timeOfDay * 24;
    int hours = (int) total_hours;
    int minutes = (int) ((total_hours - hours) * 60);
    int seconds = (int) ((((total_hours - hours) * 60) - minutes) * 60);
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  /**
   * @return The time with some additional information to be displayed to the screen.
   */
  public String getTimeFormatted() {
    String isDay = isDay() ? "Day time" : "Night time";
    return get24HourTime() + " | " + isDay + " | Day: " + day;
  }

  /**
   * Renders text of the current time of day.
   */
  public void drawTimeText(Display display) {
    String time = getTimeFormatted();
    display.drawText(time, 20, 5, 20, Color.WHITE);
  }

  /**
   * Draws a transparent black rect, opacity depending on time of night.
   * @param display The display to draw the effect onto.
   */
  public void drawDarknessEffect(Display display) {
    double lightLevel = Math.min(timeOfDay, 1 - timeOfDay);

    lightLevel *= 2;
    lightLevel = Math.min(lightLevel, 0.8) + 0.2; // Small period of time of full lightness.

    double alpha = 0.6 * (1 - lightLevel);
    display.drawTransparentRectangle(0, 0, display.getWidth(), display.getHeight(), alpha, Color.BLACK);
  }
}
