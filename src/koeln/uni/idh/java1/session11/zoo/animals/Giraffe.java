package koeln.uni.idh.java1.session11.zoo.animals;

public class Giraffe extends WalkingMammal {
	private final String name;

	public Giraffe(String name) {
		this.name = name;
		super.name = name;
		System.out.println("A giraffe named " + name + " has arrived at the zoo.");
	}

	public void stretchNeck() {
		System.out.println(name + " stretches its neck high into the air.");
	}

	@Override
	public char getSymbol() {
		return 'G';
	}
}
