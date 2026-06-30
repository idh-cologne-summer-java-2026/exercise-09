package koeln.uni.idh.java1.session11.zoo.animals;

public class Elephant extends WalkingMammal {

  @Override
  public char getSymbol() {
    return 'E';
  }
  
  public void eat(String food) {
	  System.out.println("Der Elefant frisst " + food + ".");
  }

}
