package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.Season;

public class Zebra extends WalkingMammal {
	private final String name;

	public Zebra(String name) {
		this.name = name;
		super.name = name;
		System.out.println("A zebra named " + name + " trots into the zoo.");
	}

	public void runFast() {
		System.out.println(name + " runs swiftly across the plains.");
	}

	@Override
	public boolean canReproduceInSeason(Season season) {
		return season == Season.AUTUMN;
	}

	@Override
	public char getSymbol() {
		return 'Z';
	}
}
