package koeln.uni.idh.java1.session11.zoo.animals;

public class Antelope extends WalkingMammal {

	public Antelope() {
		this.stepsize = 3; // schnellstes Tier
	}

	@Override
	public char getSymbol() {
		return 'A';
	}
}