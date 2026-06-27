package koeln.uni.idh.java1.session11.zoo.ui;

/**
 * Mehrzeilige ASCII-Bilder der Tiere für den Kampfbildschirm. Ausgewählt wird
 * über das Karten-Symbol des Tieres (A/H/E/L/D/K).
 */
public final class Sprites {

	private static final String[] ALPAKA = {
		"  (\\_/)  ",
		"  (o.o)  ",
		"  (> <)  ",
		"  /| |\\  ",
	};

	private static final String[] PFERD = {
		"   _~^~_   ",
		"  / o o \\  ",
		"  >  Y  <  ",
		"  /_| |_\\  ",
	};

	private static final String[] ELEFANT = {
		"   __  __   ",
		"  /  \\/  \\  ",
		" ( o    o )=",
		"  \\__/\\__/  ",
	};

	private static final String[] LOEWE = {
		"  .vvVvv.  ",
		" ( o   o ) ",
		"  \\  w  /  ",
		"  /~| |~\\  ",
	};

	private static final String[] ADLER = {
		"  \\\\v//  ",
		"  (o-o)  ",
		"  /]_[\\  ",
		"   ^ ^   ",
	};

	private static final String[] KROKODIL = {
		"  ___________  ",
		" [_o________VV ",
		" <____________>",
		"  ~~~~~~~~~~~~  ",
	};

	private static final String[] UNKNOWN = {
		"  ?????  ",
		"  ?   ?  ",
		"  ?????  ",
		"         ",
	};

	/** Großes Block-Logo „ZOOKEMON" für den Titelbildschirm. */
	private static final String[] LOGO = {
		"█████ █████ █████ ██  █ █████ █   █ █████ █   █",
		"   ██ ██ ██ ██ ██ ██ ██ ██    ██ ██ ██ ██ ██  █",
		"  ██  ██ ██ ██ ██ ████  ████  █ █ █ ██ ██ █ █ █",
		" ██   ██ ██ ██ ██ ██ ██ ██    █   █ ██ ██ █  ██",
		"█████ █████ █████ ██  █ █████ █   █ █████ █   █",
	};

	/** Bedrohliches ASCII-Bildnis von Prof. Nils für die Bosskampf-Inszenierung. */
	private static final String[] NILS = {
		"        .-\"\"\"\"\"-.        ",
		"       /  _   _  \\       ",
		"      |  (o) (o)  |      ",
		"      |     >     |      ",
		"      |   '---'   |      ",
		"       \\  vvvvv  /       ",
		"     ___'-.___.-'___     ",
		"    /   P R O F.     \\   ",
		"   /    N I L S       \\  ",
	};

	private Sprites() {
	}

	public static String[] titleLogo() {
		return LOGO;
	}

	public static String[] nils() {
		return NILS;
	}

	public static String[] forSymbol(char symbol) {
		switch (symbol) {
		case 'A':
			return ALPAKA;
		case 'H':
			return PFERD;
		case 'E':
			return ELEFANT;
		case 'L':
			return LOEWE;
		case 'D':
			return ADLER;
		case 'K':
			return KROKODIL;
		default:
			return UNKNOWN;
		}
	}
}
