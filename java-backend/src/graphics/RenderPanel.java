package graphics;

import java.awt.Color;

/**
 * Interface for rendering graphics to a panel. This panel can be 
 * a GUI panel or a web panel.
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public interface RenderPanel {
  public void fill(Color color);

	public void drawCircle(int x, int y, int radius, Color color);

	public void drawRect(int x, int y, int width, int height, Color color, boolean filled);

	public void drawEqualTriangle(int centerX, int centerY, int radius, Color color);
  
	public void drawText(String text, int fontSize, int x, int y, Color color);
  
	public void drawLine(int x1, int y1, int x2, int y2, Color color);

	public void drawTransparentRect(int x, int y, int width, int height, Color color, double alpha);

	public void update();
}
