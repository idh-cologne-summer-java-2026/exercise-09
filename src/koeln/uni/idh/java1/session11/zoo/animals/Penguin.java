package koeln.uni.idh.java1.session11.zoo.animals;

public class Penguin extends WalkingMammal {
	public Penguin() {
		this.name = "Pixel";
		this.stepsize = 1;
	}

	@Override
	public char getSymbol() {
		return 'P';
	}
}
