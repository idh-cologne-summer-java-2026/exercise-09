package koeln.uni.idh.java1.session11.zoo;

/**
 * Implementiert von allem, das auf Zeitänderungen im Zoo reagieren soll.
 */
public interface DayNightAware {
	void onTimeChange(int hour);
}