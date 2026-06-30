package koeln.uni.idh.java1.session11.zoo.animals;

public class Alpaca extends WalkingMammal {
	public Alpaca() {
		super("Alpaka");
		System.out.println("A new alpaca has been born.");
	}
	
	@Override
	public char getSymbol() {
		return 'A';
	}
	
	public void spit() {
		System.out.println("Ein Alpaka spuckt.");
	}
}
