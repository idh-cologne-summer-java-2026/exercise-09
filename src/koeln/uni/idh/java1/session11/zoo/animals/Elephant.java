package koeln.uni.idh.java1.session11.zoo.animals;

public class Elephant extends WalkingMammal {
  
  public Elephant() {
    setupFeedingSchedule();
  }
  
  /**
   * Sets up the default feeding schedule for elephants
   */
  private void setupFeedingSchedule() {
    addFeedingSchedule(new FeedingSchedule(6, 0, Food.VEGETABLES, "breakfast"));
    addFeedingSchedule(new FeedingSchedule(12, 30, Food.FRUIT, "lunch"));
    addFeedingSchedule(new FeedingSchedule(18, 0, Food.VEGETABLES, "dinner"));
  }

  @Override
  public char getSymbol() {
    return 'E';
  }
  
  /**
   * Gets the preferred food types for elephants
   */
  public Food[] getPreferredFoods() {
    return new Food[]{Food.VEGETABLES, Food.FRUIT, Food.GRASS, Food.GRAIN};
  }
  
  /**
   * Gets the primary preferred food for elephants
   */
  public Food getPreferredFood() {
    return Food.VEGETABLES;
  }

}
