package view;

import entities.generic.Entity;
import graphics.Display;
import simulation.Simulator;

/**
 * Combines the Simulator and Display to visualise the simulation
 */
public class Engine {
    private final Display display;
    private final Simulator simulator;
    private final Clock clock;

    private boolean running = false;

    private final int FPS;
    private final double fieldScaleFactor; //Scales the field size up/down, so field size doesn't have to be screen size
    /*
    0 < scaleFactor < 1 => field is zoomed in
    scaleFactor = 1 => field is screen size (1 field unit = 1px)
    scale factor > 1 => field is zoomed out
    e.g. fieldScaleFactor = 0.5 -> field size is 1/2 screen size (and so is entity render size)
     */

    /**
     * Create an engine to run the simulation
     * @param displayWidth the width of the GUI display
     * @param displayHeight the height of the GUI display
     * @param fps fps to run simulation at
     */
    public Engine(int displayWidth, int displayHeight, int fps, double fieldScaleFactor) {
        FPS = fps;
        this.fieldScaleFactor = fieldScaleFactor;
        int fieldWidth = (int) (displayWidth * fieldScaleFactor);
        int fieldHeight = (int) (displayHeight * fieldScaleFactor);

        simulator = new Simulator(fieldWidth, fieldHeight);
        display = new Display(displayWidth, displayHeight);
        clock = new Clock(FPS);
    }

    /**
     * Initialise Engine with FPS set to 60
     */
    public Engine(int displayWidth, int displayHeight, double fieldScaleFactor) {
        this(displayWidth, displayHeight, 60, fieldScaleFactor);
    }


    /**
     * Main loop of the simulation
     */
    private void run(){
        while (running){
            simulator.step();
            display.clear();

            for (Entity entity : simulator.getEntities()) {
                entity.draw(display, fieldScaleFactor);
            }

            display.update();
            clock.tick();
        }
    }

    /**
     * Start simulation asynchronously on a new thread so that it can be stopped in the main execution thread
     */
    public void start() {
        running = true;
        Thread t = new Thread(this::run);
        t.start();
    }
}
