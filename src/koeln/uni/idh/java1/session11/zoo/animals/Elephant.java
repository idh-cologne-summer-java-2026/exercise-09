package koeln.uni.idh.java1.session11.zoo.animals;

public class Elephant extends WalkingMammal {
	public Elephant() {
		this.name = "Unknown Elephant";
		System.out.println("A new elephant has been born.");
	}

	public Elephant(String name) {
		this.name = name;
		System.out.println("A new elephant named " + name + " has been born.");
	}

	@Override
	public char getSymbol() {
		return 'E';
	}

	public void trumpet() {
		System.out.println("The elephant trumpets loudly!");
	}
}
