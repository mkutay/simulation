package simulation.environment;

import graphics.Display;
import util.Utility;
import util.Vector;

import java.awt.*;

/**
 * Lightning effect that spawns during storms. This effect is purely 
 * visual and does not interact with the simulation.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Lightning {
  private Vector[] positions; // Stores a list of the points that make up the lightning.
  private int age = 0; // How long the lightning has been alive for.
  private int maxAge = 0; // How long the lightning should live for.
  private final int numSegments; // Number of segments that make the lightning.

  private final static int MAX_AGE = 100; // Maximum that the maxAge can be.
  private final static int MIN_AGE = 80; // Minimum that the maxAge can be.

  private final static int MIN_SEGMENTS = 3; // Minimum number of segments in the lightning.
  private final static int MAX_SEGMENTS = 6; // Maximum number of segments in the lightning.


  /**
   * Constructor -- Spawn a new lightning bolt.
   */
  public Lightning(Display display) {
    numSegments = (int) Utility.lerp(MIN_SEGMENTS, MAX_SEGMENTS, Math.random());
    generateSegmentPositions(display.getWidth(), display.getHeight());
    maxAge = (int) Utility.lerp(MIN_AGE, MAX_AGE, Math.random());
  }

  /**
   * Generate the segments of the lightning bolt. The segments and the entire
   * lightning are going to be inside the width and the height.
   * @param width The width of the display.
   * @param height The height of the display.
   */
  private void generateSegmentPositions(double width, double height) {
    double spawnX = (0.1 + 0.9 * Math.random()) * width;
    positions = new Vector[numSegments + 1];
    positions[0] = new Vector(spawnX, 0);

    double totalHeight = (0.4 + 0.6 * Math.random()) * height;

    for (int i = 0; i < numSegments; i++) {
      double segmentX = positions[i].x() + (Math.random() - 0.5) * width * 0.45;
      double segmentY = positions[i].y() + totalHeight/ numSegments * (0.9 + 0.1 * Math.random());
      positions[i + 1] = new Vector(segmentX, segmentY);
    }
  }

  /**
   * Draw the entire lightning bolt on the display.
   * @param display The display to draw the lightning on.
   */
  public void draw(Display display) {
    for (int i = 1; i < numSegments + 1; i++) {
      int x1 = (int) positions[i - 1].x();
      int y1 = (int) positions[i - 1].y();
      int x2 = (int) positions[i].x();
      int y2 = (int) positions[i].y();

      display.drawLine(x1, y1, x2, y2, getColor());
    }
  }

  /**
   * Color fades into black over lifetime.
   */
  private Color getColor() {
    int value = (int) Utility.lerp(255, 0, (double) age / maxAge);
    return new Color(value, value, value);
  }

  /**
   * Increment the age of the lightning.
   */
  public void incrementAge() {
    age++;
  }

  /**
   * Check if the lightning's lifetime is over.
   */
  public boolean isDead() {
    return age >= maxAge;
  }
}