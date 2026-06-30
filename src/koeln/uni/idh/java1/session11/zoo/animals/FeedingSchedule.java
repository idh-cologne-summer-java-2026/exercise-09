package koeln.uni.idh.java1.session11.zoo.animals;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a scheduled feeding time for an animal.
 * Tracks when an animal should be fed and with what type of food.
 */
public class FeedingSchedule {
	private LocalTime feedingTime;
	private Food foodType;
	private String description;
	private boolean completed;
	
	/**
	 * Creates a feeding schedule for a specific time with a specific food
	 * 
	 * @param hour The hour of the day (0-23)
	 * @param minute The minute of the hour (0-59)
	 * @param foodType The type of food to provide
	 * @param description A description of this feeding (e.g., "breakfast", "lunch")
	 */
	public FeedingSchedule(int hour, int minute, Food foodType, String description) {
		this.feedingTime = LocalTime.of(hour, minute);
		this.foodType = foodType;
		this.description = description;
		this.completed = false;
	}
	
	public LocalTime getFeedingTime() {
		return feedingTime;
	}
	
	public Food getFoodType() {
		return foodType;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	/**
	 * Check if it's time to feed based on current time
	 * Returns true if the current time is within 5 minutes of the scheduled feeding time
	 */
	public boolean isTimeToFeed() {
		LocalTime now = LocalTime.now();
		int diffMinutes = Math.abs(now.getHour() * 60 + now.getMinute() - 
		                           feedingTime.getHour() * 60 + feedingTime.getMinute());
		return diffMinutes <= 5 && !completed;
	}
	
	/**
	 * Reset the schedule for the next day
	 */
	public void reset() {
		this.completed = false;
	}
	
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return "📅 " + description + " at " + feedingTime.format(formatter) + 
		       " (" + foodType.getName() + ")" + (completed ? " ✓" : "");
	}
}
