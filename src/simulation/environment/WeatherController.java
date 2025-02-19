package simulation.environment;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import graphics.Display;
import simulation.simulationData.Data;
import util.Vector;

/**
 * Stores the weather and updates it accordingle.
 * The weather can be one of the following, with the corresponding effects:
 * CLEAR - No effect.
 * RAINING - Plants multiply faster.
 * WINDY - Random vector pushes animals around.
 * STORM - Animals move slower + windy effects.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class WeatherController {
  private double windDirection; // The direction of the wind in radians.
  private Weather weather; // The current weather.

  private final static int PARTICLE_SPAWN_RATE = 4; // The number of rain particles to spawn per update.
  private final List<RainParticle> rainParticles; // The rain particles on the screen.

  private static final double LIGHTNING_SPAWN_PROBABILITY = 0.03; // The probability of spawning a lightning bolt.
  private final List<Lightning> lightnings; // The lightning bolts on the screen.
  
  /**
   * Constructor -- Creates a new weather controller with a random weather and wind direction.
   */
  public WeatherController() {
    setRandomWeather();
    windDirection = Math.random() * Math.PI * 2;
    rainParticles = new ArrayList<>();
    lightnings = new ArrayList<>();
  }

  /**
   * Sets the current weather to be a random weather. The new weather cannot be the 
   * same as the current weather.
   */
  private void setRandomWeather() {
    List<Weather> weathers = new ArrayList<>(Arrays.asList(Weather.values()));
    weathers.remove(weather); // Ensure that the new weather won't be the same one.
    int randomIndex = (int) (Math.random() * weathers.size());
    weather = weathers.get(randomIndex);
  }

  /**
   * Updates the wind direction.
   */
  public void updateWeather() {
    double turbulence = weather == Weather.STORM ? 0.1 : 0.05;
    windDirection += (Math.random() - 0.5) * Math.PI * turbulence;
  }

  /**
   * Changes the weather randomly.
   */
  public void changeWeather() {
    if (Math.random() < Data.getWeatherChangeProbability()) {
      setRandomWeather();
    }
  }

  /**
   * Draws the weather effects on the screen; that is, the lightning and rain effects.
   * Also spawns the lightning bolts and the rain.
   * @param display The display to draw the effects onto.
   */
  public void drawWeatherEffects(Display display) {
    spawnRain(display);
    spawnLightning(display);

    rainParticles.removeIf(p -> p.isOutOfBounds(display));
    for (RainParticle particle : rainParticles) {
      particle.update(display, getWindVector());
    }

    lightnings.removeIf(Lightning::isDead);
    for (Lightning lightning : lightnings) {
      lightning.draw(display);
      lightning.incrementAge();
    }
  }

  /**
   * Draws the weather text, specifying the current weather and wind direction.
   */
  public void drawWeatherText(Display display) {
    display.drawText(weather.toString(), 20, 5, 40, Color.WHITE);
    if (weather == Weather.WINDY || weather == Weather.STORM) {
      display.drawText("Wind Direction:", 20, 5, 60, Color.WHITE);
      display.drawCircle(180, 55, 20, Color.BLACK);
      display.drawArrow(180, 55, windDirection, 20, Color.WHITE);
    }
  }

  /**
   * Spawns rain particles on the top section of the screen. Some particles spawn out of
   * bounds on the left and right side of the screen, so they will "seem" as coming from the sides.
   * @param display The display to spawn the rain particles onto.
   */
  private void spawnRain(Display display) {
    if (weather == Weather.CLEAR || weather == Weather.WINDY) return;

    for (int i = 0; i < PARTICLE_SPAWN_RATE; i++) {
      // Spawn the particles on the top section of the screen.
      Vector spawnPosition = new Vector((Math.random() - 0.5) * display.getWidth() * 4, 1);
      rainParticles.add(new RainParticle(spawnPosition));
    }
  }

  /**
   * Spawns a lightning bolt on the screen with a certain probability.
   * Spawns lightning bolts only when there is a storm.
   * @param display The display to draw the lightning onto.
   */
  private void spawnLightning(Display display) {
    if (weather != Weather.STORM) return;

    if (Math.random() < LIGHTNING_SPAWN_PROBABILITY) {
      lightnings.add(new Lightning(display));
    }
  }

  /**
   * @return The wind vector using the wind direction.
   */
  public Vector getWindVector() {
    return Vector.getVectorFromAngle(windDirection);
  }

  public Weather getWeather() { return weather; }
  public double getWindDirection() { return windDirection; }
}
