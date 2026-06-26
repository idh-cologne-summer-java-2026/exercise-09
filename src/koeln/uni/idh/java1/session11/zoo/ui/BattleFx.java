package koeln.uni.idh.java1.session11.zoo.ui;

import koeln.uni.idh.java1.session11.zoo.battle.Battler;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

/**
 * Transiente Effekt-Informationen für einen einzelnen Kampf-Frame: ein
 * fliegendes Projektil, ein Treffer-Blitz auf einem Tier und ein
 * Bildschirm-Wackeln. Alle Felder sind optional (null bzw. 0).
 */
public class BattleFx {

	/** Tier, das gerade getroffen wurde und rot aufblitzt (oder null). */
	public Battler flash;

	/** Horizontaler Versatz für den Screen-Shake (0 = kein Wackeln). */
	public int shake;

	/** Typ des fliegenden Projektils (null = kein Projektil). */
	public Type projectileType;

	/** Spalte des Projektils auf der Effekt-Zeile. */
	public int projectileColumn;
}
