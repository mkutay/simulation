package simulation;

import simulation.simulationData.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.Vector;

/**
 * Stores the weather and the time of day
 * CLEAR - No effect
 * RAINING - Plants multiply faster
 * WINDY - Random vector pushes animals around :)
 * STORM - Animals move slower + Windy effects
 */
public class Environment {
    //Daytime is 8am to 8pm
    private int lastUpdateDay = 1;
    private int day = 1;
    private double timeOfDay = 0.3333; // Loops from 0 to 1. 1/3 is 8am (day time start)
    private double windDirection;
    private Weather weather;

    public Environment() {
        setRandomWeather();
    }

    /**
     * Sets the current weather to be a random weather (that isn't the same as the previous weather)
     */
    private void setRandomWeather() {
        List<Weather> weathers = new ArrayList<>(Arrays.asList(Weather.values()));
        weathers.remove(weather); //Ensure that the new weather won't be the same one
        int randomIndex = (int) (Math.random() * weathers.size());
        weather = weathers.get(randomIndex);
    }

    /**
     * Updates the wind direction and the current weather when the day changes
     */
    public void updateWeather() {
        windDirection += (Math.random() - 0.5) * Math.PI * 0.01;

        if (lastUpdateDay != day){
            lastUpdateDay = day;
            if (Math.random() < Data.getWeatherChangeProbability()) {
                setRandomWeather();
            }
        }
    }

    /**
     * Increments the time of day and the current day.
     * @param dayNightCycleRate the amount of time to pass per update.
     */
    public void incrementTime(double dayNightCycleRate) {
        timeOfDay += dayNightCycleRate;
        if (timeOfDay >= 1) {timeOfDay = 0; day++;};
    }

    /**
     * Daytime is any time from 8am to 8pm.
     * @return true if the time is day
     */
    public boolean isDay(){
        return timeOfDay <= 0.8333 && timeOfDay >= 0.3333;
    }

    /**
     * Converts the time of day value from 0-1 to a 24-hour clock time
     * @return the time of day in the 24-hour format HH:MM:SS
     */
    private String get24HourTime() {
        double total_hours = timeOfDay * 24;
        int hours = (int) total_hours;
        int minutes = (int) ((total_hours - hours) * 60);
        int seconds = (int) ((((total_hours - hours) * 60) - minutes) * 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * @return The time with some additional information to be displayed to the screen
     */
    public String getTimeFormatted(){
        String isDay = isDay() ? "Day time" : "Night time";
        return get24HourTime() + " | " + isDay + " | Day : " + day;
    }

    public Weather getWeather() {
        return weather;
    }

    public Vector getWindVector() {
        return Vector.getVectorFromAngle(windDirection);
    }

}
