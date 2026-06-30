package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

public class VisitorFeedingTest {
	public static void main(String[] args) {
		WalkingMammal animal = new Alpaca();
		animal.setHunger(80);
		animal.setThirst(70);

		Visitor visitor = new Visitor("Mina");
		visitor.feedAnimal(animal);

		if (animal.getHunger() >= 80 || animal.getThirst() >= 70) {
			throw new IllegalStateException("Feeding did not reduce hunger and thirst.");
		}

		System.out.println("Feeding test passed.");
	}
}
