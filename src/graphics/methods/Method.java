package graphics.methods;

public abstract class Method {
  public int i;
  public int[] c;

  public Method(int id, int[] colour) {
    this.i = id;
    this.c = colour;
  }
}
