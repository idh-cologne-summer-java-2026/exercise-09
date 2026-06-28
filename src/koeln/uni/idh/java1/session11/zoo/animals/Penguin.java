package koeln.uni.idh.java1.session11.zoo.animals;

public class Penguin extends WalkingMammal {

  public Penguin() {
    System.out.println("A playful penguin waddles into the zoo.");
  }

  @Override
  public char getSymbol() {
    return 'P';
  }

  public void slide() {
    System.out.println("The penguin slides across the ice.");
  }
}
