package koeln.uni.idh.java1.session11.zoo;

import java.util.Random;

public enum Weather {
	NORMAL("mild", 0, 0, 0),
	RAIN("Regen", 0, -1, 0),
	HEATWAVE("Hitzewelle", 1, 3, 2),
	COLD("Kaelte", 1, 0, 1),
	STORM("Sturm", 2, 1, 2);

	private final String label;
	private final int hungerEffect;
	private final int thirstEffect;
	private final int stressEffect;

	Weather(String label, int hungerEffect, int thirstEffect, int stressEffect) {
		this.label = label;
		this.hungerEffect = hungerEffect;
		this.thirstEffect = thirstEffect;
		this.stressEffect = stressEffect;
	}

	public static Weather random(Random random) {
		int roll = random.nextInt(100);
		if (roll < 45) {
			return NORMAL;
		}
		if (roll < 65) {
			return RAIN;
		}
		if (roll < 80) {
			return HEATWAVE;
		}
		if (roll < 92) {
			return COLD;
		}
		return STORM;
	}

	public String getLabel() {
		return label;
	}

	public int getHungerEffect() {
		return hungerEffect;
	}

	public int getThirstEffect() {
		return thirstEffect;
	}

	public int getStressEffect() {
		return stressEffect;
	}
}
