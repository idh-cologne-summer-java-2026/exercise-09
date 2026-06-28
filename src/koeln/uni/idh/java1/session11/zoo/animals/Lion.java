package koeln.uni.idh.java1.session11.zoo.animals;

public class Lion extends WalkingMammal {

  public Lion() {
    System.out.println("A powerful lion has joined the zoo.");
  }

  @Override
  public char getSymbol() {
    return 'L';
  }

  public void roar() {
    System.out.println("The lion roars loudly.");
  }
}
