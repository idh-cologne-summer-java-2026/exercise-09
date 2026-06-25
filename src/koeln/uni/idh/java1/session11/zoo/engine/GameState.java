package koeln.uni.idh.java1.session11.zoo.engine;

/**
 * Ein Zustand des Spiels (State-Pattern). Der {@link Game} zeichnet den
 * aktuellen Zustand und leitet Tastendrücke an ihn weiter. Ein Zustand kann
 * über {@code game.setState(...)} zu einem anderen wechseln.
 */
public interface GameState {

	/** Zeichnet den Zustand auf den Bildschirm. */
	void render();

	/**
	 * Verarbeitet einen Tastendruck.
	 *
	 * @param key die gedrückte (kleingeschriebene) Taste
	 */
	void handleInput(char key);
}
