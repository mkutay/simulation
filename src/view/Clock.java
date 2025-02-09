package view;

/**
 * Handles delay between simulation steps at correct FPS.
 */
public class Clock {
  private final double fps;
  private double lastTick; // Time in nanoseconds
  private double deltaTime;

  public Clock(int fps) {
    this.fps = fps;
    lastTick = System.nanoTime();
  }

  /**
   * Waits a clock cycle (1 / fps) by waiting in a while loop.
   */
  public void tick() {
    while (System.nanoTime() < lastTick + (double) 1_000_000_000 / fps) { }
    deltaTime = System.nanoTime() - lastTick;
    lastTick = System.nanoTime();
  }

  /**
   * Useful for debug or performance testing.
   * @return The current FPS of the simulation.
   */
  public double getCurrentFps() {
    return fps / getDeltaTime();
  }

  public double getDeltaTime() {
    return deltaTime * fps / 1_000_000_000;
  }
}