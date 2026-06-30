package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.Season;

public class Lion extends WalkingMammal {
	private final String name;

	public Lion(String name) {
		this.name = name;
		super.name = name;
		System.out.println("A lion named " + name + " has entered the enclosure.");
	}

	public void roar() {
		System.out.println(name + " lets out a mighty roar.");
	}

	@Override
	public boolean canReproduceInSeason(Season season) {
		return season == Season.SUMMER;
	}

	@Override
	public char getSymbol() {
		return 'L';
	}
}
