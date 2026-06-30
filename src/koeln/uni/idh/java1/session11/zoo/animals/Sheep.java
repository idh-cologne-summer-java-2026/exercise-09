package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.Season;

public class Sheep extends WalkingMammal {
	private final String name;

	public Sheep(String name) {
		this.name = name;
		super.name = name;
		System.out.println("A sheep named " + name + " joins the pasture.");
	}

	public void baa() {
		System.out.println(name + " says baa.");
	}

	@Override
	public boolean canReproduceInSeason(Season season) {
		return season == Season.SPRING;
	}

	@Override
	public char getSymbol() {
		return 'S';
	}
}
