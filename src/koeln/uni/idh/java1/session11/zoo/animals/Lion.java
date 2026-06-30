package koeln.uni.idh.java1.session11.zoo.animals;

/**
 * A lion. Like every WalkingMammal it can walk and turn; it only needs to say
 * which character represents it on the zoo field.
 */
public class Lion extends WalkingMammal {

	public Lion(String name) {
		this.name = name;
		this.stepsize = 2; // lions are faster than the default step of 1
		System.out.println("Lion " + name + " enters the zoo.");
	}

	@Override
	public char getSymbol() {
		return 'L';
	}

	public void roar() {
		System.out.println("Lion " + name + " roars: ROAAAR!");
	}
}
