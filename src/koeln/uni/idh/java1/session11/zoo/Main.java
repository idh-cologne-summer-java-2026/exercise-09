package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * ZOO SIMULATION - "GO WILD" UPDATE
 * ---------------------------------------------------------
 * Diese Klasse erweitert den ursprünglichen Zoo um mehrere 
 * dynamische und interaktive Features (mithilfe von KI-Agenten):
 * 
 * 1. Nutzer-Interaktion: Die Zeit läuft nicht automatisch ab, 
 *    sondern wird durch Konsoleneingabe (Enter-Taste) gesteuert.
 * 2. Begrenzte Welt: Die Tiere prallen an den Rändern ab.
 * 3. Hitzewelle: Kann ein-/ausgeschaltet werden. Tiere werden 
 *    verwirrt und wechseln ständig zufällig die Richtung.
 * 4. T-Rex (Raubtier): Kann per Knopfdruck gespawnt werden 
 *    (implementiert als anonyme Klasse). Frisst andere Tiere.
 * 5. Fortpflanzung: Wenn zwei Alpakas auf denselben Koordinaten 
 *    landen, wird ein neues Alpaka-Baby geboren.
 * ---------------------------------------------------------
 */
public class Main {

	public static void main(String[] args) {
		// 1. Vorbereitungen für das Spielfeld
		int breite = 30;
		int hoehe = 15;
		List<WalkingMammal> tiere = new ArrayList<>();
		Random rand = new Random();
		Scanner scanner = new Scanner(System.in);
		boolean hitzewelle = false;

		// 2. Start-Tiere in den Zoo setzen und zufällig platzieren
		tiere.add(platziereTier(new Alpaca(), breite, hoehe, rand));
		tiere.add(platziereTier(new Alpaca(), breite, hoehe, rand)); // Zwei Alpakas für mögliche Babys!
		tiere.add(platziereTier(new Elephant(), breite, hoehe, rand));
		tiere.add(platziereTier(new Horse("Fury", "black"), breite, hoehe, rand));

		System.out.println("=== WILLKOMMEN IM INTERAKTIVEN CHAOS-ZOO ===");

		// 3. Die Endlosschleife für die Simulation
		while (true) {
			AsciiImage ai = new AsciiImage(breite, hoehe);
			
			// HACK: Da AsciiImage nur WalkingMammals zeichnen kann, 
			// erschaffen wir einen "Baum-Dummy", der nicht in der Tier-Liste 
			// auftaucht (sich also nie bewegt), aber ein 'T' zeichnet.
			WalkingMammal baumDummy = new WalkingMammal() {
				@Override
				public char getSymbol() { return 'T'; }
			};
			
			// Statische Bäume pflanzen
			ai.dot(5, 5, baumDummy);
			ai.dot(24, 12, baumDummy);
			ai.dot(15, 3, baumDummy);

			// Alle echten Tiere ins Bild zeichnen
			for (WalkingMammal tier : tiere) {
				// Sicherheitscheck: Tiere am Rand "abprallen" lassen
				if (tier.getX() < 0) tier.setX(0);
				if (tier.getX() >= breite) tier.setX(breite - 1);
				if (tier.getY() < 0) tier.setY(0);
				if (tier.getY() >= hoehe) tier.setY(hoehe - 1);

				ai.dot(tier.getX(), tier.getY(), tier);
			}

			// Zoo ausgeben
			System.out.println(ai.toString());
			System.out.println("Anzahl der Tiere im Zoo: " + tiere.size());
			if (hitzewelle) {
				System.out.println("!!! ACHTUNG: HITZWELLE AKTIV. DIE TIERE DREHEN DURCH !!!");
			}

			// 4. Nutzer-Interaktion abfragen
			System.out.println("Aktionen: [Enter] = Zeit vergeht | [h] = Hitzewelle | [t] = T-Rex | [q] = Beenden");
			System.out.print("Deine Wahl: ");
			String eingabe = scanner.nextLine().trim().toLowerCase();

			if (eingabe.equals("q")) {
				System.out.println("Der Zoo schließt. Tschüss!");
				break;
			} else if (eingabe.equals("h")) {
				hitzewelle = !hitzewelle; // Schaltet Hitzewelle an oder aus
			} else if (eingabe.equals("t")) {
				// T-Rex wird hier als anonyme Klasse direkt erzeugt
				WalkingMammal trex = new WalkingMammal() {
					@Override
					public char getSymbol() { return 'R'; } // R für T-Rex
				};
				trex.setX(breite / 2); // Spawnt in der Mitte
				trex.setY(hoehe / 2);
				tiere.add(trex);
				System.out.println("\n*** ALARM! EIN T-REX (R) IST AUSGEBROCHEN! ***");
			}

			// 5. Update: Alle Tiere bewegen lassen und Kollisionen prüfen
			List<WalkingMammal> neueBabys = new ArrayList<>();
			List<WalkingMammal> gefresseneTiere = new ArrayList<>();

			for (WalkingMammal aktuellesTier : tiere) {
				// Tiere drehen sich manchmal zufällig. Bei Hitzewelle immer!
				if (rand.nextBoolean() || hitzewelle) {
					int drehRichtung = rand.nextBoolean() ? 1 : -1;
					aktuellesTier.turn(drehRichtung);
				}
				aktuellesTier.walk();

				// Kollisions-Prüfung: Treffen sich zwei Tiere auf demselben Feld?
				for (WalkingMammal anderesTier : tiere) {
					if (aktuellesTier != anderesTier && aktuellesTier.getX() == anderesTier.getX() && aktuellesTier.getY() == anderesTier.getY()) {
						
						// Fall 1: Der T-Rex frisst jemanden (außer sich selbst)
						if (aktuellesTier.getSymbol() == 'R' && anderesTier.getSymbol() != 'R') {
							if (!gefresseneTiere.contains(anderesTier)) {
								gefresseneTiere.add(anderesTier);
								System.out.println(">> Oh nein! Der T-Rex hat ein Tier gefressen! <<");
							}
						}
						// Fall 2: Zwei Alpakas treffen sich -> Baby-Alpaka!
						else if (aktuellesTier.getSymbol() == 'A' && anderesTier.getSymbol() == 'A') {
							System.out.println(">> Liebe im Zoo! Zwei Alpakas haben sich getroffen. Ein Baby wurde geboren! <<");
							Alpaca baby = new Alpaca();
							baby.setX(aktuellesTier.getX());
							baby.setY(aktuellesTier.getY());
							neueBabys.add(baby);
						}
					}
				}
			}

			// Babys hinzufügen und gefressene Tiere entfernen
			tiere.addAll(neueBabys);
			tiere.removeAll(gefresseneTiere);
			
			System.out.println("\n------------------------------------------------\n");
		}
		scanner.close();
	}

	// Hilfsmethode, um Tieren beim Start eine zufällige Position zu geben
	private static WalkingMammal platziereTier(WalkingMammal tier, int maxBreite, int maxHoehe, Random rand) {
		tier.setX(rand.nextInt(maxBreite));
		tier.setY(rand.nextInt(maxHoehe));
		return tier;
	}
}