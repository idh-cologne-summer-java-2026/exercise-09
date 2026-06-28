package koeln.uni.idh.java1.session11.zoo.animals;

public class Elephant extends WalkingMammal {

  @Override
  public char getSymbol() {
    return 'E';
  }

  @Override
  public String getAsciiArt() {
    return "   " + getDisplayName() + "\n"
        + "   /\\  ___\n"
        + "  ( o )  )\n"
        + "   \\__/  \n";
  }

}
