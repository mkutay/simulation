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

    private final static int FPS = 30;

    public Engine(int displayWidth, int displayHeight) {
        simulator = new Simulator(displayWidth, displayHeight);
        display = new Display(displayWidth, displayHeight);
        clock = new Clock(FPS);
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
