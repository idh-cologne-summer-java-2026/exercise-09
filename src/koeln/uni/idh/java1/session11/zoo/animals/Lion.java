package koeln.uni.idh.java1.session11.zoo.animals;

public class Lion extends WalkingMammal {
	public Lion() {
		this.name = "Luna";
		this.stepsize = 2;
	}

	@Override
	public char getSymbol() {
		return 'L';
	}
}
