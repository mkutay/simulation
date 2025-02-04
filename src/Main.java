import view.Engine;

public class Main {
    public static void main(String[] args) {
        int fps = 60;
        double fieldScaleFactor = 0.5;
        Engine engine = new Engine(600, 600, fps, fieldScaleFactor);
        engine.start();
    }
}
