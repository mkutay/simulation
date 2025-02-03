package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * A modified JPanel to allow easy graphics rendering.
 * All graphics are drawn onto the BufferedImage "surface" through the Graphics2D attribute.
 * Updates are shown when repaint() is called.
 */
public class RenderPanel extends JPanel {
    private final Graphics2D g2;
    private final BufferedImage surface;

    /**
     * Creates the render panel
     *
     * @param screenWidth  the screen width of the display (px)
     * @param screenHeight the screen height of the display (px)
     */
    public RenderPanel(int screenWidth, int screenHeight) {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        surface = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        g2 = (Graphics2D) surface.getGraphics(); //Get graphics context
    }

    /**
     * Fills the screen with a single colour
     * @param color color to draw to the screen with
     */
    public void fill(Color color){
        g2.setColor(color);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Draws a filled circle
     * @param x x position of center
     * @param y y position of center
     * @param radius radius in px
     * @param color colour to render circle
     */
    public void drawCircle(int x, int y, int radius, Color color){
        g2.setColor(color);
        g2.fillOval(x - radius, y - radius, radius*2, radius*2);
    }

    /**
     * Clears all drawn elements
     */
    public void clear() {
        g2.clearRect(0, 0, getWidth(), getHeight());
    }


    /**
     * Called with every draw call, draws everything stored on the bufferedImage to the display
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(surface, 0, 0, null);
    }
}

