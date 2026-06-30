package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

public class Visitor {
	private final String name;

	public Visitor(String name) {
		this.name = name;
	}

	public void feedAnimal(WalkingMammal animal) {
		animal.eat();
		animal.drink();
		System.out.println(name + " fed " + animal.getClass().getSimpleName() + " and gave it water.");
	}
}
