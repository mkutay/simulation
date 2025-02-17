package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * A modified JPanel to allow easy graphics rendering.
 * All graphics are drawn onto the BufferedImage "surface" through the Graphics2D attribute.
 * Updates are shown when repaint() is called.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class RenderPanel extends JPanel {
	private final Graphics2D g2; // Graphics context
	private final BufferedImage surface; // The buffered image to draw to.

	/**
	 * Constructor -- Create the render panel.
	 * @param screenWidth the screen width of the display (px).
	 * @param screenHeight the screen height of the display (px).
	 */
	public RenderPanel(int screenWidth, int screenHeight) {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		surface = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);

		g2 = (Graphics2D) surface.getGraphics(); // Get graphics context.
	}

	/**
	 * Fills the screen with a single colour.
	 */
	public void fill(Color color) {
		g2.setColor(color);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * Draw a filled circle.
	 * @param x X position of the circle's center.
	 * @param y Y position of the circle's center.
	 * @param radius Radius in pixels.
	 * @param color Colour to render circle.
	 */
	public void drawCircle(int x, int y, int radius, Color color) {
		g2.setColor(color);
		g2.fillOval(x - radius, y - radius, radius*2, radius*2);
	}

	/**
	 * Draw a filled rectangle.
	 * @param x X position of top left.
	 * @param y Y position of top left.
	 * @param width Width in pixels.
	 * @param height Height in pixels.
	 * @param color Colour to render rectangle.
	 * @param filled To draw the rectangle filled or not
	 */
	public void drawRect(int x, int y, int width, int height, Color color, boolean filled) {
		g2.setColor(color);
		if (filled) {
			g2.fillRect(x, y, width, height);
		} else {
			g2.drawRect(x, y, width, height);
		}
	}

	/**
	 * Draw a filled equilateral triangle.
	 * @param centerX X position of the triangle's center.
	 * @param centerY Y position of the triangle's center.
	 * @param radius Radius in pixels.
	 * @param color Colour to render triangle.
	 */
	public void drawEqualTriangle(int centerX, int centerY, int radius, Color color) {
		g2.setColor(color);
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];

		for (int i = 0; i < 3; i++) {
			double angle = Math.PI / 2 + i * 2 * Math.PI / 3;
			xPoints[i] = (int) (centerX + (radius * Math.cos(angle)));
			yPoints[i] = (int) (centerY - (radius * Math.sin(angle)));
		}

		g2.fillPolygon(xPoints, yPoints, 3);
	}

	/**
	 * Renders text - the baseline of the first character is at position (x, y)
	 * @param text the text to draw onto the screen
	 * @param fontSize the size of the text to draw
	 * @param x the x pos of the first character
	 * @param y the y pos of the first character
	 * @param color the colour of the text to render
	 */
	public void drawText(String text, int fontSize, int x, int y, Color color) {
		g2.setColor(color);
		g2.setFont(new Font("Consolas", Font.PLAIN, fontSize));
		g2.drawString(text, x, y);
	}

	/**
	 * Draws a line segment from (x1,y1) to (x2,y2)
	 * @param color the colour of the line
	 */
	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		g2.setColor(color);
		g2.drawLine(x1, y1, x2, y2);
	}

	/**
	 * Draw a transparent rectangle
	 * @param x X position of top left
	 * @param y Y position of top left
	 * @param width Width in pixels
	 * @param height Height in pixels
	 * @param color Colour to render rectangle
	 * @param alpha Transparency level (0 transparent, 1 opaque)
	 */
	public void drawTransparentRect(int x, int y, int width, int height, Color color, double alpha) {
		Composite originalComposite = g2.getComposite();

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
		g2.setColor(color);
		g2.fillRect(x, y, width, height);

		g2.setComposite(originalComposite);
	}

	/**
	 * Called with every draw call, draws everything stored on the bufferedImage to the display.
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(surface, 0, 0, null);
	}
}