package simulation.environment;

import graphics.Display;
import util.Utility;
import util.Vector;

import java.awt.*;

/**
 * Lightning effect that spawns during storms
 * Another effect just because it is cool
 */
public class Lightning {
    private Vector[] positions; //Stores a list of the points of lightning
    private int age = 0;
    private int maxAge = 0;
    private final int numSegments;

    private final static int MAX_AGE = 100;
    private final static int MIN_AGE = 80;

    private final static int MIN_SEGMENTS = 3;
    private final static int MAX_SEGMENTS = 6;


    /**
     * Spawn a new lightning bolt
     */
    public Lightning(Display display) {
        numSegments = (int) (MIN_SEGMENTS + (MAX_SEGMENTS - MIN_SEGMENTS) * Math.random());
        generatePositions(display.getWidth(), display.getHeight());
        maxAge = (int) (MIN_AGE + (MAX_AGE - MIN_AGE) * Math.random());
    }

    /**
     * Generate the segments of the lightning bolt
     */
    private void generatePositions(double maxWidth, double maxHeight) {
        double spawnX = (0.1 + 0.9 * Math.random()) * maxWidth;
        positions = new Vector[numSegments + 1];
        positions[0] = new Vector(spawnX, 0);

        double totalHeight = (0.4 + 0.6 * Math.random()) * maxHeight;

        for (int i = 0; i < numSegments; i++) {
            double segmentX = positions[i].x() + (Math.random() - 0.5) * maxWidth * 0.45;
            double segmentY = positions[i].y() + totalHeight/ numSegments * (0.9 + 0.1 * Math.random());
            positions[i+1] = new Vector(segmentX, segmentY);
        }
    }

    public void incrementAge() {
        age++;
    }

    public void draw(Display display) {
        for (int i = 1; i < numSegments +1; i++) {
            int x1 = (int) positions[i-1].x();
            int y1 = (int) positions[i-1].y();
            int x2 = (int) positions[i].x();
            int y2 = (int) positions[i].y();

            display.drawLine(x1, y1, x2, y2, getColor());
        }
    }

    /**
     * Color fades into black over lifetime
     */
    private Color getColor() {
        int value = (int) Utility.lerp(255, 0, (double) age/maxAge);
        return new Color(value, value, value);
    }

    public boolean isDead() {
        return age > maxAge;
    }
}


