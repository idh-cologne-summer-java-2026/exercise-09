package koeln.uni.idh.java1.session11.zoo.animals;

public class Horse extends WalkingMammal {
	private static int numberOfHorses = 0;
	private String color;

	public Horse(String name, String color) {
		super(name);
		numberOfHorses++;
		this.color = color;
		setStepsize(2);
		System.out.println("Horse " + name + " has been born and has " + color + " fur. There are now "
				+ numberOfHorses + " horses in the world.");
	}

	public Horse mate(Horse partner) {
		Horse child = new Horse("Child of " + getName() + " and " + partner.getName(),
				mixFurColors(this.color, partner.color));
		System.out.println("Horse " + child.getName() + " has been born.");
		return child;
	}

	private String mixFurColors(String fur1, String fur2) {
		if (fur1.equals("black") || fur2.equals("black"))
			return "black";
		if (fur1.equals("brown"))
			return fur2 + "-" + fur1;
		return fur1 + "-" + fur2;
	}

	@Override
	public char getSymbol() {
		return 'H';
	}

	public String getColor() {
		return color;
	}
}
