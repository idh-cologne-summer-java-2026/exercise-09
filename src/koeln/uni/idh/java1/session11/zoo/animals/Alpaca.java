package koeln.uni.idh.java1.session11.zoo.animals;

public class Alpaca extends WalkingMammal {
	public Alpaca() {
		setupFeedingSchedule();
		System.out.println("A new alpaca has been born.");
	}
	
	/**
	 * Sets up the default feeding schedule for alpacas
	 */
	private void setupFeedingSchedule() {
		addFeedingSchedule(new FeedingSchedule(6, 30, Food.GRASS, "early breakfast"));
		addFeedingSchedule(new FeedingSchedule(13, 0, Food.VEGETABLES, "lunch"));
		addFeedingSchedule(new FeedingSchedule(19, 30, Food.HAY, "dinner"));
	}
	
	@Override
	public char getSymbol() {
		return 'A';
	}
	
	public void spit() {
		System.out.println("Ein Alpaka spuckt.");
	}
	
	/**
	 * Gets the preferred food types for alpacas
	 */
	public Food[] getPreferredFoods() {
		return new Food[]{Food.GRASS, Food.VEGETABLES, Food.HAY, Food.FRUIT};
	}
	
	/**
	 * Gets the primary preferred food for alpacas
	 */
	public Food getPreferredFood() {
		return Food.GRASS;
	}
}
