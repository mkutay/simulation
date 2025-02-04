import view.Engine;

public class Main {
    public static void main(String[] args) {
        int fps = 0;
        Engine engine = new Engine(600, 600, fps);
        engine.start();
    }
}
