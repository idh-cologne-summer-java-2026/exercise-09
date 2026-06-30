package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert die Uhrzeit im Zoo und benachrichtigt registrierte
 * Beobachter (z.B. Tiere), wenn sich die Stunde ändert.
 */
public class Clock {

	/** Aktuelle Stunde, 0-23 */
	private int hour = 6;

	private List<DayNightAware> listeners = new ArrayList<>();

	public void register(DayNightAware listener) {
		listeners.add(listener);
	}

	/**
	 * Schaltet die Uhr um eine Stunde weiter und informiert alle
	 * registrierten Tiere über die neue Uhrzeit.
	 */
	public void tick() {
		hour = (hour + 1) % 24;
		System.out.println("--- Es ist jetzt " + hour + " Uhr ---");
		for (DayNightAware listener : listeners) {
			listener.onTimeChange(hour);
		}
	}

	public int getHour() {
		return hour;
	}

	/**
	 * Tag gilt von 6 bis 21 Uhr (exklusiv 22), Nacht von 22 bis 5 Uhr.
	 */
	public boolean isDay() {
		return hour >= 6 && hour < 22;
	}
}
