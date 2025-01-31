package entities;

public class Location {
  public double x;
  public double y;

  public Location(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Calculate the distance between "this" location and another location
   * @param loc The other location
   * @return The distance between the two locations
   */
  public double distance(Location loc) {
    return Math.sqrt(Math.pow(loc.x - x, 2) + Math.pow(loc.y - y, 2));
  }
}
