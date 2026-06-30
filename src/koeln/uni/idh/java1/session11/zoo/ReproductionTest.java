package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

public class ReproductionTest {
	public static void main(String[] args) {
		WalkingMammal alpaca = new Alpaca();
		WalkingMammal offspringInSeason = alpaca.reproduce(Season.SPRING);
		if (offspringInSeason == null) {
			throw new IllegalStateException("Alpaca should reproduce in spring.");
		}

		WalkingMammal lion = new Lion("Leo");
		WalkingMammal offspringOutOfSeason = lion.reproduce(Season.WINTER);
		if (offspringOutOfSeason != null) {
			throw new IllegalStateException("Lion should not reproduce in winter.");
		}

		System.out.println("Reproduction test passed.");
	}
}
