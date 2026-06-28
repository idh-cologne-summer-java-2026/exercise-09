package koeln.uni.idh.java1.session11.zoo.animals;

public class Alpaca extends WalkingMammal {
	public Alpaca() {
		System.out.println("A new alpaca has been born.");
	}
	
	@Override
	public char getSymbol() {
		return 'A';
	}

	@Override
	public String getAsciiArt() {
		return "   " + getDisplayName() + "\n"
			+ "   /\\_/\\\n"
			+ "  ( o.o )\n"
			+ "   > ^ <\n";
	}
	
	public void spit() {
		System.out.println("Ein Alpaka spuckt.");
	}
}
