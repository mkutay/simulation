package graphics.methods;

public abstract class Method {
  public String n;
  public int[] c;

  public Method(String name, int[] colour) {
    this.n = name;
    this.c = colour;
  }
}
