package koeln.uni.idh.java1.session11.zoo.animals;

public class Zebra extends WalkingMammal {
	public Zebra() {
		System.out.println("A new zebra has been born.");
	}

	@Override
	public char getSymbol() {
		return 'Z';
	}
}
