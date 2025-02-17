package simulation;

import graphics.Display;
import util.Utility;
import util.Vector;

import java.awt.*;

/**
 * Creates a falling rain particle for visual effect
 * just is cool :)
 */
public class RainParticle {
    private Vector position;
    private Vector lastPosition;
    private final Color color;

    private static final double GRAVITY = 5;

    public RainParticle(Vector position) {
        this.position = position;
        lastPosition = position;
        color = Utility.mutateColor(new Color(51, 210, 242), 1, 0.25);
    }

    public void update(Display display, Vector windVector) {
        draw(display);
        lastPosition = position;
        updatePosition(windVector);
    }

    private void updatePosition(Vector windVector) {
        Vector gravityVector = new Vector(0, GRAVITY);
        Vector moveVector = windVector.add(gravityVector);
        position = position.add(moveVector);
    }

    private void draw(Display display) {
        display.drawLine((int) position.x(), (int) position.y(), (int) lastPosition.x(), (int) lastPosition.y(), color);
    }

    public boolean isOutOfBounds(Display display) {
        double height = display.getHeight();

        return position.y() > height;
    }
}
