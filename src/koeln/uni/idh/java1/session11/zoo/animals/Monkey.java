package koeln.uni.idh.java1.session11.zoo.animals;

public class Monkey extends WalkingMammal {

	public Monkey() {
		this.stepsize = 1;
	}

	@Override
	public char getSymbol() {
		return 'M';
	}
}