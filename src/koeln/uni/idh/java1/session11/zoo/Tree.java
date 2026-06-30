package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public class Tree implements Drawable {
  private final int x;
  private final int y;

  public Tree() {
    this(0, 0);
  }

  public Tree(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public char getSymbol() {
    return 'T';
  }

}
