package util;

public class Utility {
    public static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }
}
