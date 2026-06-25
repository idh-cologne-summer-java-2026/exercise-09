package koeln.uni.idh.java1.session11.zoo.animals;

public class Giraffe extends WalkingMammal {

	public Giraffe() {
		this.stepsize = 2; // etwas schneller
	}

	@Override
	public char getSymbol() {
		return 'G';
	}
}