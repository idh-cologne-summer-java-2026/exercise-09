package koeln.uni.idh.java1.session11.zoo.animals;

public class Horse extends WalkingMammal {
	static int numberOfHorses = 0;

	/**
	 * How many "fields" does the horse move if it takes a single step?
	 */
	int stepsize = 1;

	/**
	 * The name of the horse
	 */
	String name;

	/**
	 * The color of the horse's fur
	 */
	String color;

	/**
	 * Horses need to have a name and a fur color. There is no technical reason
	 * here, it just shows some variation of constructors.
	 * 
	 * @param name
	 * @param color
	 */
	public Horse(String name, String color) {
		numberOfHorses++;
		this.name = name;
		this.color = color;
		setupFeedingSchedule();
		System.out.println("Horse " + name + " has been born and has " + color + " fur. There are now " + numberOfHorses + " horses in the world.");
	}
	
	/**
	 * Sets up the default feeding schedule for horses
	 */
	private void setupFeedingSchedule() {
		addFeedingSchedule(new FeedingSchedule(7, 0, Food.GRAIN, "breakfast"));
		addFeedingSchedule(new FeedingSchedule(12, 0, Food.HAY, "lunch"));
		addFeedingSchedule(new FeedingSchedule(18, 0, Food.GRASS, "dinner"));
	}

	/**
	 * Two horses mate and create a new horse.
	 * 
	 * @param partner The partner of the current horse
	 * @return A newly born horse
	 */
	public Horse mate(Horse partner) {
		Horse ret = new Horse("Child of " + this.name + " and " + partner.name,
				mixFurColors(this.color, partner.color));
		System.out.println("Horse " + ret.name + " has been born.");
		return ret;
	}
	
	/**
	 * Gets the preferred food type for horses
	 */
	public Food getPreferredFood() {
		return Food.GRASS;
	}
	
	/**
	 * Horses prefer hay over other foods for optimal nutrition
	 */
	public Food[] getPreferredFoods() {
		return new Food[]{Food.GRAIN, Food.HAY, Food.GRASS, Food.VEGETABLES};
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
	
	

}