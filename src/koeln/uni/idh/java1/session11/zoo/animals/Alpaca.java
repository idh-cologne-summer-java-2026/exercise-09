package koeln.uni.idh.java1.session11.zoo.animals;

public class Alpaca extends WalkingMammal {
	public Alpaca() {
		this("Alfi");
	}

	public Alpaca(String name) {
		super(name);
		System.out.println("A new alpaca named " + name + " has been born.");
	}

	@Override
	public char getSymbol() {
		return 'A';
	}

	public void spit() {
		System.out.println(getName() + " spuckt. Typisch Alpaka!");
	}
}
