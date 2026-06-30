package koeln.uni.idh.java1.session11.zoo.animals;

public class Cat extends WalkingMammal {

	public Cat(String name) {
		this.name = name;
		System.out.println("A new cat named " + name + " has arrived.");
	}

	@Override
	public char getSymbol() {
		return 'C';
	}
}
