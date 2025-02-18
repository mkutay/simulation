package simulation.environment;

import graphics.Display;
import util.Utility;
import util.Vector;

import java.awt.*;

/**
 * Creates a falling rain particle purely for some visual effect. The rain particle 
 * consists of a position and a last position, where the line segment that makes the rain
 * is between the current position and the last position.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class RainParticle {
  private Vector position; // The current position of the rain particle
  private Vector lastPosition; // The last position of the rain partcile.
  private final Color color; // The color of the rain particle

  private static final Vector GRAVITY_VECTOR = new Vector(0, 5); // The gravity that affects a rain particle.

  /**
   * Constructor -- Creates a rain particle with a given position.
   * @param position The position of the rain particle.
   */
  public RainParticle(Vector position) {
    this.position = position;
    this.lastPosition = position;
    // The colour of the rain particle is some shade of blue:
    color = Utility.mutateColor(new Color(51, 210, 242), 1, 0.25);
  }

  /**
   * Updates the rain particle's position based on the given wind vector and
   * draws the rain particle.
   * @param display The display to draw the rain particle on.
   * @param windVector The wind vector that affects the rain particle.
   */
  public void update(Display display, Vector windVector) {
    draw(display);
    lastPosition = position;
    updatePosition(windVector);
  }

  /**
   * Updates the position of the rain particle based on the wind vector and gravity.
   * @param windVector The wind vector that affects the rain particle.
   */
  private void updatePosition(Vector windVector) {
    Vector moveVector = windVector.multiply(1.5).add(GRAVITY_VECTOR);
    position = position.add(moveVector);
  }

  /**
   * Draws the rain particle on the display by displaying a line between the current
   * position and the last position.
   * @param display The display to draw the rain particle on.
   */
  private void draw(Display display) {
    display.drawLine((int) position.x(), (int) position.y(), (int) lastPosition.x(), (int) lastPosition.y(), color);
  }

  /**
   * @implNote Only the bottom side of the display is checked if the rain particle is out of bounds.
   * This is because the rain particles can be drawn from the sides of the display.
   * @see Environment.spawnRain
   * @param display The display to check if the rain particle is out of bounds of.
   * @return True if the rain particle is out of bounds of the display, false otherwise.
   */
  public boolean isOutOfBounds(Display display) {
    double height = display.getHeight();

    return position.y() > height;
  }
}