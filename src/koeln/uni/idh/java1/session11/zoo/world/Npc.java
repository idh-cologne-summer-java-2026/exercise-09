package koeln.uni.idh.java1.session11.zoo.world;

/**
 * Eine ansprechbare Figur in der Welt, die <em>kein</em> Zookémon ist (z. B. ein
 * Kommilitone oder eine K.I.). NPCs stehen an einem festen Ort, blockieren wie
 * Prof. Nils ihr Feld (man spricht sie an, statt darauf zu treten) und geben dem
 * Spieler beim ersten Gespräch einen Buff.
 *
 * Diese Klasse hält nur die welt-/darstellungsrelevanten Daten (Position, Glyph,
 * Art, „schon gesprochen?"). Die konkreten Dialogtexte und Buffs liegen bewusst
 * in der Spiellogik (engine), damit die Welt nichts davon wissen muss.
 */
public class Npc {

	private final NpcKind kind;
	private final char glyph;
	private final int x;
	private final int y;
	private boolean talkedTo = false;

	public Npc(NpcKind kind, char glyph, int x, int y) {
		this.kind = kind;
		this.glyph = glyph;
		this.x = x;
		this.y = y;
	}

	public NpcKind getKind() {
		return kind;
	}

	/** Das Sonderzeichen, mit dem der NPC auf der Karte dargestellt wird. */
	public char getGlyph() {
		return glyph;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/** Ob der Spieler bereits mit dem NPC gesprochen (und den Buff erhalten) hat. */
	public boolean isTalkedTo() {
		return talkedTo;
	}

	public void setTalkedTo(boolean talkedTo) {
		this.talkedTo = talkedTo;
	}
}
