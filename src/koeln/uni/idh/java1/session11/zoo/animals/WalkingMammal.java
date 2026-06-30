package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents walking mammals. Walking mammals have a position (x and
 * y coordinates), a face direction, a step size (i.e., the number of units they
 * go when making a single step) and a name.
 * 
 * Walking mammals can turn and walk, and they know how they should be
 * represented in a zoo visualization.
 * 
 * @author nils.reiter@uni-koeln.de
 *
 */
public abstract class WalkingMammal implements Drawable {
	protected String name = "Unknown Animal";

	/**
	 * the current x position of the mammal
	 */
	int x = 1;

	/**
	 * The current y position of the mammal
	 */
	int y = 1;
	
	/**
	 * How far the animal walks in a single step
	 */
	int stepsize = 1;

	/**
	 * The current view direction of the horse, on a 360° wheel (compass rose).
	 * 0 => top, 90 => right, 180 => bottom, 270 => left
	 */
	int direction = 0;
	
	/**
	 * Hunger level (0-100). 0 = not hungry, 100 = starving
	 */
	int hunger = 30;
	
	/**
	 * Thirst level (0-100). 0 = not thirsty, 100 = very thirsty
	 */
	int thirst = 20;
	
	/**
	 * Time elapsed in "ticks" since the animal was last fed
	 */
	long lastFedTime = System.currentTimeMillis();
	
	/**
	 * Time elapsed in "ticks" since the animal last drank
	 */
	long lastDrinkTime = System.currentTimeMillis();
	
	/**
	 * Feeding schedule for this animal
	 */
	List<FeedingSchedule> feedingSchedules = new ArrayList<>();

	/**
	 * The animal walks a single step in the direction in which it is looking.
	 */
	public void walk() {

		switch (direction) {
		case 0:
			this.y = this.y - stepsize;
			break;
		case 180:
			this.x = this.x - stepsize;
			break;
		case 270:
			this.y = this.y + stepsize;
			break;
		case 90:
			this.x = this.x + stepsize;
		}
		System.out.println("Animal has moved.");
	}

	/**
	 * This method calculates the new direction by taking the sign of the argument
	 * with Math.signum(), multiplying that with 90 and add it to the old direction
	 * value. To avoid that we produce direction values > 360, we take the modulo of
	 * 360.
	 * 
	 * @param turnDirection If the argument is a negative number, the animal turns
	 *                      to the left. If it's positive number, it turns to the
	 *                      right.
	 */
	public void turn(int turnDirection) {
		this.direction = (int) (this.direction + (Math.signum(turnDirection) * 90) % 360);
		System.out.println("Animal " + name + " has turned and is now looking towards " + direction + ".");

	}
	
	/**
	 * Feeds the animal with the given food type. Reduces hunger based on food satiation.
	 * 
	 * @param food The type of food to feed to the animal
	 */
	public void feed(Food food) {
		int previousHunger = this.hunger;
		this.hunger = Math.max(0, this.hunger - food.getHungerSatiation());
		this.thirst = Math.min(100, this.thirst + food.getThirstSatiation() / 10);
		this.lastFedTime = System.currentTimeMillis();
		System.out.println(name + " has been fed " + food.getName() + ". Hunger reduced from " + previousHunger + " to " + this.hunger + ".");
	}
	
	/**
	 * The animal drinks water. Reduces thirst significantly.
	 */
	public void drink() {
		int previousThirst = this.thirst;
		this.thirst = Math.max(0, this.thirst - 40);
		this.lastDrinkTime = System.currentTimeMillis();
		System.out.println(name + " has drunk water. Thirst reduced from " + previousThirst + " to " + this.thirst + ".");
	}
	
	/**
	 * Simulates the passage of time. Hunger and thirst increase over time.
	 * This should be called periodically in a game loop.
	 */
	public void updateNeeds() {
		long currentTime = System.currentTimeMillis();
		long timeSinceFed = (currentTime - lastFedTime) / 1000; // Convert to seconds
		long timeSinceDrink = (currentTime - lastDrinkTime) / 1000;
		
		// Increase hunger based on time
		this.hunger = Math.min(100, this.hunger + (int)(timeSinceFed / 10));
		
		// Increase thirst based on time
		this.thirst = Math.min(100, this.thirst + (int)(timeSinceDrink / 5));
		
		// Update feeding/drinking times
		this.lastFedTime = currentTime;
		this.lastDrinkTime = currentTime;
	}
	
	/**
	 * Gets the current hunger level
	 * @return Hunger level (0-100)
	 */
	public int getHunger() {
		return hunger;
	}
	
	/**
	 * Gets the current thirst level
	 * @return Thirst level (0-100)
	 */
	public int getThirst() {
		return thirst;
	}
	
	/**
	 * Returns a string representation of the animal's needs
	 */
	public String getNeedsStatus() {
		String hungerStatus = hunger < 30 ? "satisfied" : hunger < 70 ? "hungry" : "starving";
		String thirstStatus = thirst < 30 ? "hydrated" : thirst < 70 ? "thirsty" : "very thirsty";
		return name + " - Hunger: " + hunger + "/100 (" + hungerStatus + "), Thirst: " + thirst + "/100 (" + thirstStatus + ")";
	}
	
	/**
	 * Adds a feeding schedule for this animal
	 * 
	 * @param schedule The FeedingSchedule to add
	 */
	public void addFeedingSchedule(FeedingSchedule schedule) {
		feedingSchedules.add(schedule);
		System.out.println("📅 Feeding schedule added for " + name + ": " + schedule);
	}
	
	/**
	 * Gets all feeding schedules for this animal
	 */
	public List<FeedingSchedule> getFeedingSchedules() {
		return feedingSchedules;
	}
	
	/**
	 * Checks if it's time to feed and notifies accordingly
	 * Returns true if it's time to feed
	 */
	public boolean checkFeedingTime() {
		for (FeedingSchedule schedule : feedingSchedules) {
			if (schedule.isTimeToFeed()) {
				notifyHungry(schedule);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks hunger level and provides notification
	 */
	public void checkHungerStatus() {
		if (hunger > 80) {
			System.out.println("⚠️  ALERT: " + name + " is STARVING (Hunger: " + hunger + "/100)!");
		} else if (hunger > 60) {
			System.out.println("⚠️  WARNING: " + name + " is very hungry (Hunger: " + hunger + "/100)");
		} else if (hunger > 40) {
			System.out.println("ℹ️  INFO: " + name + " is hungry (Hunger: " + hunger + "/100)");
		}
		
		if (thirst > 80) {
			System.out.println("🚨 CRITICAL: " + name + " is very thirsty (Thirst: " + thirst + "/100)!");
		} else if (thirst > 60) {
			System.out.println("⚠️  WARNING: " + name + " needs water (Thirst: " + thirst + "/100)");
		}
	}
	
	/**
	 * Sends a hunger notification
	 */
	private void notifyHungry(FeedingSchedule schedule) {
		System.out.println("🔔 FEEDING TIME: " + name + " should be fed " + 
		                 schedule.getDescription() + " (" + schedule.getFoodType().getName() + ")");
		schedule.setCompleted(true);
	}
	
	/**
	 * Displays detailed feeding information
	 */
	public void displayFeedingInfo() {
		System.out.println("\n🐾 === Feeding Information for " + name + " ===");
		if (feedingSchedules.isEmpty()) {
			System.out.println("  No feeding schedules set.");
		} else {
			System.out.println("  📋 Scheduled Feedings:");
			for (FeedingSchedule schedule : feedingSchedules) {
				System.out.println("    " + schedule);
			}
		}
		System.out.println(getNeedsStatus());
	}

	/**
	 * How to represent the animal on the zoo field. Note that this is not an
	 * individual animal, but one that symbolizes the class of the animal.
	 * 
	 * @return A character used to represent the animal
	 */
	public abstract char getSymbol();

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
