package koeln.uni.idh.java1.session11.zoo.animals;

public class Monkey extends WalkingMammal {
	private final String name;

	public Monkey(String name) {
		this.name = name;
		super.name = name;
		System.out.println("A monkey named " + name + " swings into the zoo.");
	}

	public void swing() {
		System.out.println(name + " swings from branch to branch.");
	}

	@Override
	public char getSymbol() {
		return 'M';
	}
}
