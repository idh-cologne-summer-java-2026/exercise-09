package koeln.uni.idh.java1.session11.zoo.animals;

/**
 * This enum represents different types of food that can be fed to animals.
 * Each food type has associated properties like nutritional value and satiation.
 */
public enum Food {
	GRASS(10, 30, "grass", "Natural vegetation", "Low", "High"),
	HAY(15, 40, "hay", "Dried grass - great for herbivores", "Medium", "High"),
	VEGETABLES(12, 35, "vegetables", "Fresh produce rich in vitamins", "Medium", "High"),
	MEAT(20, 25, "meat", "Protein-rich food for carnivores", "High", "Low"),
	FRUIT(8, 28, "fruit", "Sweet and nutritious", "Low", "Medium"),
	GRAIN(18, 45, "grain", "Energy-dense carbohydrates", "High", "High");

	/**
	 * How much this food reduces hunger (0-100)
	 */
	private final int hungerSatiation;
	
	/**
	 * How much this food reduces thirst (0-100)
	 */
	private final int thirstSatiation;
	
	/**
	 * Human-readable name of the food
	 */
	private final String name;
	
	/**
	 * Detailed description of the food
	 */
	private final String description;
	
	/**
	 * Nutritional density (Low, Medium, High)
	 */
	private final String nutritionDensity;
	
	/**
	 * Digestibility rating (Low, Medium, High)
	 */
	private final String digestibility;

	Food(int hungerSatiation, int thirstSatiation, String name, 
	     String description, String nutritionDensity, String digestibility) {
		this.hungerSatiation = hungerSatiation;
		this.thirstSatiation = thirstSatiation;
		this.name = name;
		this.description = description;
		this.nutritionDensity = nutritionDensity;
		this.digestibility = digestibility;
	}

	public int getHungerSatiation() {
		return hungerSatiation;
	}

	public int getThirstSatiation() {
		return thirstSatiation;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getNutritionDensity() {
		return nutritionDensity;
	}
	
	public String getDigestibility() {
		return digestibility;
	}
	
	@Override
	public String toString() {
		return name + " - " + description + "\n" +
		       "  📊 Nutrition: " + nutritionDensity + " | Digestibility: " + digestibility + "\n" +
		       "  🍽️ Satiation - Hunger: " + hungerSatiation + ", Thirst: " + thirstSatiation;
	}
}

