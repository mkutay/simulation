package graphics;

import javax.swing.JFrame;
import java.awt.*;

/**
 * Display acts as the main surface to render graphics to.
 * Once you have rendered the desired graphics, call update() to
 * draw them to the screen.
 * 
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class Display {
  private final RenderPanel renderPanel; // The panel to render to
  private final JFrame display;

  /**
   * Constructor -- Create a new display with the specified screen width and height.
   */
  public Display(int screenWidth, int screenHeight) {
    display = new JFrame("Window");
    renderPanel = new RenderPanel(screenWidth, screenHeight);

    display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    display.setResizable(false);

    display.add(renderPanel);
    display.pack(); // Force correct sizing

    display.setVisible(true);
    display.createBufferStrategy(2);
  }

  /**
   * Update the display to show the rendered graphics.
   */
  public void update() {
    renderPanel.repaint();
  }

  /**
   * Fill the entire screen with the specified color.
   */
  public void fill(Color color) {
    renderPanel.fill(color);
  }

  /**
   * Draw a circle at the specified position with the specified radius and color.
   */
  public void drawCircle(int x, int y, int radius, Color color) {
    renderPanel.drawCircle(x, y, radius, color);
  }

  /**
   * Draw a rectangle at the specified position with the specified width, height, and color.
   * @param filled draw a filled rectangle if true, outline only if false
   */
  public void drawRectangle(int x, int y, int width, int height, Color color, boolean filled) {
    renderPanel.drawRect(x, y, width, height, color, filled);
  }

  /**
   * Draw a filled rectangle at the specified position with the specified width, height, and color.
   */
  public void drawRectangle(int x, int y, int width, int height, Color color) {
    renderPanel.drawRect(x, y, width, height, color, true);
  }

  /**
   * Draw an equilateral triangle at the specified centre position with the specified radius and color.
   */
  public void drawEqualTriangle(int centerX, int centerY, int radius, Color color) {
    renderPanel.drawEqualTriangle(centerX, centerY, radius, color);
  }

  /**
   * Draws the given text with the first character at the given x,y
   */
  public void drawText(String text, int fontSize, int x, int y, Color color) {
    renderPanel.drawText(text, fontSize, x, y, color);
  }
}
