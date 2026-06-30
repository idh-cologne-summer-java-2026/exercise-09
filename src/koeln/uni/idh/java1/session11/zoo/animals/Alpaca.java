package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.Season;

public class Alpaca extends WalkingMammal {
	public Alpaca() {
		System.out.println("A new alpaca has been born.");
	}
	
	@Override
	public boolean canReproduceInSeason(Season season) {
		return season == Season.SPRING;
	}

	@Override
	public char getSymbol() {
		return 'A';
	}
	
	public void spit() {
		System.out.println("Ein Alpaka spuckt.");
	}
}
