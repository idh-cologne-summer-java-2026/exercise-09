package koeln.uni.idh.java1.session11.zoo.animals;

public class Elephant extends WalkingMammal {
  public Elephant() {
    this.name = "Edda";
    this.stepsize = 1;
  }

  @Override
  public char getSymbol() {
    return 'E';
  }

}
