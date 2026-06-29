package koeln.uni.idh.java1.session11.zoo.animals;

public class Elephant extends WalkingMammal {
	public Elephant() {
		this("Ella");
	}

	public Elephant(String name) {
		super(name);
		setStepsize(1);
	}

	@Override
	public char getSymbol() {
		return 'E';
	}

	public void shower() {
		giveWater();
		System.out.println(getName() + " duscht sich mit dem Rüssel.");
	}
}
