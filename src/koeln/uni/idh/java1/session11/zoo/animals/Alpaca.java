package koeln.uni.idh.java1.session11.zoo.animals;

public class Alpaca extends WalkingMammal {
	public Alpaca() {
		this.name = "Unknown Alpaca";
		System.out.println("A new alpaca has been born.");
	}

	public Alpaca(String name) {
		this.name = name;
		System.out.println("A new alpaca named " + name + " has been born.");
	}
	
	@Override
	public char getSymbol() {
		return 'A';
	}
	
	public void spit() {
		System.out.println("Ein Alpaka spuckt.");
	}
}
