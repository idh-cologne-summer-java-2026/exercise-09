package koeln.uni.idh.java1.session11.zoo.animals;

public class Camel extends WalkingMammal {
	private final String name;

	public Camel(String name) {
		this.name = name;
		super.name = name;
		System.out.println("A camel named " + name + " wanders into the desert area.");
	}

	public void carryLoad() {
		System.out.println(name + " carries its pack across the dunes.");
	}

	@Override
	public char getSymbol() {
		return 'C';
	}
}
