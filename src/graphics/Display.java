package graphics;

import javax.swing.JFrame;
import java.awt.*;

/**
 * Display acts as the main surface to render graphics to.
 * Once you have rendered the desired graphics, call update() to
 * draw them to the screen.
 */
public class Display{
  private final RenderPanel renderPanel;

  public Display(int screenWidth, int screenHeight) {
    JFrame display = new JFrame("Window");
    renderPanel = new RenderPanel(screenWidth, screenHeight);

    display.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    display.setResizable(false);

    display.add(renderPanel);
    display.pack(); // Force correct sizing

    display.setVisible(true);
  }

  public void update() {
    renderPanel.repaint();
  }

  public void fill(Color color) {
    renderPanel.fill(color);
  }

  public void drawCircle(int x, int y, int radius, Color color) {
    renderPanel.drawCircle(x, y, radius, color);
  }

  public void drawRectangle(int x, int y, int width, int height, Color color) {
    renderPanel.drawRect(x, y, width, height, color);
  }

  public void drawEqualTriangle(int centerX, int centerY, int radius, Color color) {
    renderPanel.drawEqualTriangle(centerX, centerY, radius, color);
  }

  public void clear() {
    renderPanel.clear();
  }
}
