package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public class Tree implements Drawable {
  // Hydration level (0-100). 0 = dead/dry, 100 = fully watered
  private int hydration = 80;

  // Timestamp of the last watering action
  private long lastWateredTime = System.currentTimeMillis();

  // Threshold below which the tree is considered to need water
  private static final int NEEDS_WATER_THRESHOLD = 30;

  public Tree() {
    // default hydration already set
  }

  /**
   * Water the tree. Increases hydration and updates lastWateredTime.
   * @param amount amount to increase hydration by (0-100)
   */
  public void water(int amount) {
    if (amount < 0) {
      return;
    }
    this.hydration = Math.min(100, this.hydration + amount);
    this.lastWateredTime = System.currentTimeMillis();
    System.out.println("Tree watered. Hydration is now " + this.hydration + "/100.");
  }

  /**
   * Convenience method: water with a reasonable default amount.
   */
  public void water() {
    water(40);
  }

  /**
   * Returns true if the tree needs watering (hydration below threshold)
   */
  public boolean needsWater() {
    return this.hydration < NEEDS_WATER_THRESHOLD;
  }

  /**
   * Simulate passage of time: hydration decreases depending on elapsed seconds.
   * Should be called periodically if you want the tree to dry out over time.
   */
  public void updateNeeds() {
    long currentTime = System.currentTimeMillis();
    long secondsSinceWatered = (currentTime - lastWateredTime) / 1000;
    // lose 1 hydration point every 20 seconds (adjustable)
    int loss = (int) (secondsSinceWatered / 20);
    if (loss > 0) {
      this.hydration = Math.max(0, this.hydration - loss);
      this.lastWateredTime = currentTime;
    }
  }

  /**
   * Returns a short status string about the tree's hydration
   */
  public String getNeedsStatus() {
    if (hydration >= 70) {
      return "Tree - Hydration: " + hydration + "/100 (healthy)";
    } else if (hydration >= NEEDS_WATER_THRESHOLD) {
      return "Tree - Hydration: " + hydration + "/100 (needs attention)";
    } else {
      return "Tree - Hydration: " + hydration + "/100 (needs water!)";
    }
  }

  @Override
  public char getSymbol() {
    // represent a thirsty tree with lowercase 't'
    return needsWater() ? 't' : 'T';
  }

}
