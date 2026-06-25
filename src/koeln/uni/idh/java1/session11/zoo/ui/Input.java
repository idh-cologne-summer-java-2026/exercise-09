package koeln.uni.idh.java1.session11.zoo.ui;

import java.io.IOException;

/**
 * Tastatureingabe für das Spiel. Auf macOS/Linux wird – falls möglich – der
 * Roh-Modus des Terminals aktiviert ({@code stty raw -echo}), damit einzelne
 * Tasten ohne Enter reagieren. Klappt das nicht (z. B. in der Eclipse-Konsole
 * oder unter Windows), fällt die Klasse automatisch auf zeilenweises Lesen
 * zurück: dann muss man Taste + Enter drücken.
 */
public class Input {

	private boolean rawMode = false;

	public Input() {
		tryEnableRawMode();
	}

	private void tryEnableRawMode() {
		try {
			int code = runStty("raw -echo");
			rawMode = code == 0;
		} catch (Exception e) {
			rawMode = false;
		}
	}

	/** Stellt den normalen Terminal-Modus wieder her. */
	public void restore() {
		if (rawMode) {
			try {
				runStty("sane");
			} catch (Exception e) {
				// beim Aufräumen ignorieren
			}
		}
	}

	private int runStty(String args) throws IOException, InterruptedException {
		String[] cmd = { "sh", "-c", "stty " + args + " < /dev/tty" };
		Process p = new ProcessBuilder(cmd).inheritIO().start();
		return p.waitFor();
	}

	public boolean isRawMode() {
		return rawMode;
	}

	/**
	 * Liest eine einzelne Taste. Im Roh-Modus sofort, sonst die erste
	 * Zeichen-Taste einer Eingabezeile. Liefert kleingeschriebene Zeichen.
	 *
	 * @return die gedrückte Taste oder '\0' am Ende des Eingabestroms
	 */
	public char readKey() {
		try {
			if (rawMode) {
				int c = System.in.read();
				if (c == -1) {
					return '\0';
				}
				return Character.toLowerCase((char) c);
			}
			// Zeilenmodus: erstes nicht-leeres Zeichen einer Zeile
			int c;
			char first = '\0';
			while ((c = System.in.read()) != -1) {
				if (c == '\n') {
					break;
				}
				if (first == '\0' && c != '\r') {
					first = (char) c;
				}
			}
			return Character.toLowerCase(first);
		} catch (IOException e) {
			return '\0';
		}
	}
}
