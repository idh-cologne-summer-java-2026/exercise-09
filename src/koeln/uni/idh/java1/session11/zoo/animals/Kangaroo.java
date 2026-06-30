package koeln.uni.idh.java1.session11.zoo.animals;

/**
 * This class represents a Kangaroo, a hopping marsupial.
 * Kangaroos are fast-moving herbivores that prefer grass and vegetables.
 */
public class Kangaroo extends WalkingMammal {
	
	private int hopCount = 0;
	
	public Kangaroo() {
		setupFeedingSchedule();
		System.out.println("🦘 A new kangaroo has been born!");
	}
	
	/**
	 * Sets up the default feeding schedule for kangaroos
	 */
	private void setupFeedingSchedule() {
		addFeedingSchedule(new FeedingSchedule(5, 30, Food.GRASS, "dawn grazing"));
		addFeedingSchedule(new FeedingSchedule(14, 0, Food.VEGETABLES, "mid-day feeding"));
		addFeedingSchedule(new FeedingSchedule(20, 0, Food.HAY, "evening meal"));
	}
	
	/**
	 * Gets the preferred food types for kangaroos
	 */
	public Food[] getPreferredFoods() {
		return new Food[]{Food.GRASS, Food.VEGETABLES, Food.FRUIT, Food.HAY};
	}
	
	/**
	 * Gets the primary preferred food for kangaroos
	 */
	public Food getPreferredFood() {
		return Food.GRASS;
	}
	
	@Override
	public char getSymbol() {
		return 'K';
	}
	
	/**
	 * Kangaroos can make special hops (movement mechanic unique to this class)
	 */
	public void hop() {
		hopCount++;
		int hopDistance = 3; // Kangaroos hop further than regular stepping
		switch (direction) {
		case 0:
			this.y = this.y - hopDistance;
			break;
		case 180:
			this.x = this.x - hopDistance;
			break;
		case 270:
			this.y = this.y + hopDistance;
			break;
		case 90:
			this.x = this.x + hopDistance;
		}
		System.out.println("🦘 " + name + " has hopped! (Hop #" + hopCount + ")");
	}
	
	/**
	 * Gets the total number of hops this kangaroo has made
	 */
	public int getHopCount() {
		return hopCount;
	}
}
