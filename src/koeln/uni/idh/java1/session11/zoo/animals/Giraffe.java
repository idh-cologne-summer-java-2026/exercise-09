package koeln.uni.idh.java1.session11.zoo.animals;

public class Giraffe extends WalkingMammal {

  public Giraffe() {
    System.out.println("A tall giraffe is now in the zoo.");
  }

  @Override
  public char getSymbol() {
    return 'G';
  }

  public void stretchNeck() {
    System.out.println("The giraffe stretches its long neck.");
  }
}
