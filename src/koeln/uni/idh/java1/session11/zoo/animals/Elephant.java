package koeln.uni.idh.java1.session11.zoo.animals;

public class Elephant extends WalkingMammal {

  @Override
  public void drinkWater() {
    System.out.println("Elephant is drinking water.");
  }

  public Elephant() {
    System.out.println("A new elephant has been born!");
  }

  @Override
  public char getSymbol() {
    return 'E';
  }

}
