package koeln.uni.idh.java1.session11.zoo.animals;

public class HybridMammal extends WalkingMammal {

  private final char symbol;
  private final String species;

  public HybridMammal(char symbol, String species, String name) {
    this.symbol = symbol;
    this.species = species;
    this.name = name;
    System.out.println("A new hybrid animal has been born: " + species + ".");
  }

  @Override
  public char getSymbol() {
    return symbol;
  }

  public String getSpecies() {
    return species;
  }
}
