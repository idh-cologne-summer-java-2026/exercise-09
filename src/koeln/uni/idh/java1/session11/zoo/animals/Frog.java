package koeln.uni.idh.java1.session11.zoo.animals;

public class Frog extends WalkingMammal {

	public Frog(String name) {
		this.name = name;
		System.out.println("A new frog named " + name + " has hopped into the zoo.");
	}

	@Override
	public char getSymbol() {
		return 'F';
	}
}
