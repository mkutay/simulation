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

    /**
     * Create an engine to run the simulation
     * @param displayWidth the width of the GUI display
     * @param displayHeight the height of the GUI display
     * @param fps fps to run simulation at
     */
    public Engine(int displayWidth, int displayHeight, int fps) {
        FPS = fps;
        simulator = new Simulator(displayWidth, displayHeight);
        display = new Display(displayWidth, displayHeight);
        clock = new Clock(FPS);
    }

    /**
     * Initialise Engine with FPS set to 60
     */
    public Engine(int displayWidth, int displayHeight) {
        this(displayWidth, displayHeight, 60);
    }


    /**
     * Main loop of the simulation
     */
    private void run(){
        while (running){
            simulator.step();
            display.clear();

            for (Entity entity : simulator.getEntities()) {
                entity.draw(display);
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
