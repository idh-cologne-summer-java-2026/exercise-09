# Zoo-Kampf-Simulator – „Zookémon" 🦙⚔️🐘

Übung 9 „Go Wild!" – Erweiterung des Zoo-Simulations-Codes zu einem
Pokémon-artigen Kampfspiel.

## 1. Vision

Der Spieler steuert ein eigenes Tier durch ein ASCII-Gehege (Overworld).
Wilde Tiere laufen ebenfalls durch das Gehege. Trifft der Spieler auf ein
wildes Tier, startet ein **rundenbasierter Pokémon-Kampf**: Beide Tiere
haben Lebenspunkte (HP), Werte (Angriff/Verteidigung/Initiative), einen
**Typ** und **Attacken**. Über ein Menü wählt der Spieler seine Attacke,
Typ-Effektivität und ein bisschen Zufall entscheiden über Schaden. Wer
zuerst auf 0 HP fällt, hat verloren. Danach geht es zurück in die Overworld.

**Leitprinzip:** Groß denken, aber sauberes, verständliches OOP. Lieber ein
klar strukturiertes System mit ein paar Tierarten als ein Wust an Features.
Am Ende soll jede Zeile nachvollziehbar sein (Selbstreflexion der Aufgabe).

## 2. Kernfeatures

1. **Overworld**: ASCII-Gehege mit Terrain (Gras, Wasser, Bäume, Wände).
2. **Spieler-Tier**: per Tastatur (WASD) steuerbar.
3. **Wilde Tiere**: bewegen sich autonom (nutzt vorhandenes `walk()`/`turn()`).
4. **Begegnung**: Kollision Spieler ↔ wildes Tier → Wechsel in den Kampf.
5. **Kampfsystem (Pokémon-artig)**:
   - HP, Angriff, Verteidigung, Initiative, Level
   - Typen + Typen-Tabelle (effektiv / nicht effektiv)
   - Attacken mit Stärke, Genauigkeit, Typ, optionalem Effekt
   - Rundenlogik nach Initiative, Schadensformel, Zufalls-Faktor
   - Kampf-UI: HP-Balken, Attacken-Menü, Kampf-Log
   - Sieg / Niederlage / Flucht
6. **Polish**: ANSI-Farben, Unicode-/Emoji-Symbole, Statuseffekte.

## 3. Architektur (Pakete & Klassen)

Basis-Paket: `koeln.uni.idh.java1.session11.zoo`

```
zoo
├── Main.java                 (Einstiegspunkt: startet Game)
├── Tree.java                 (bleibt, evtl. Drawable-Anpassung)
│
├── animals/                  Tier-Modell + Kampf-Eigenschaften
│   ├── WalkingMammal.java    (Basisklasse: Position, Bewegung – erweitert)
│   ├── Animal.java  (NEU)    (Battler-Brücke: HP, Stats, Typ, Moves)  *optional*
│   ├── Horse.java
│   ├── Elephant.java
│   ├── Alpaca.java
│   ├── Lion.java    (NEU)
│   ├── Eagle.java   (NEU)
│   └── Crocodile.java (NEU)
│
├── battle/                   Kampf-Logik (UI-frei, gut testbar)
│   ├── Battler.java (NEU)    Interface: getName, Stats, Typ, Moves, HP …
│   ├── Type.java    (NEU)    enum: NORMAL, ERDE, WASSER, FEUER, PFLANZE, LUFT
│   ├── TypeChart.java (NEU)  Typ-Effektivitäts-Matrix → Multiplikator
│   ├── Move.java    (NEU)    Attacke: Name, Typ, Stärke, Genauigkeit, Effekt
│   ├── Stats.java   (NEU)    maxHp, attack, defense, speed
│   ├── Status.java  (NEU)    enum: KEINER, VERGIFTET, BENOMMEN … (Statuseffekt)
│   ├── DamageCalculator.java (NEU) Schadensformel
│   └── Battle.java  (NEU)    Kampf-Engine: Runden, Sieg/Niederlage
│
├── world/                    Overworld
│   ├── World.java   (NEU)    Spielfeld: Terrain + Entities + Kollision
│   ├── Tile.java    (NEU)    enum/Klasse: GRAS, WASSER, BAUM, WAND
│   └── Player.java  (NEU)    Spieler-Steuerung (hält das Spieler-Tier)
│
├── engine/                   Spielablauf (State-Pattern)
│   ├── Game.java    (NEU)    Hauptschleife, hält aktuellen GameState
│   ├── GameState.java (NEU)  Interface: update(), render(), handleInput()
│   ├── OverworldState.java (NEU)
│   └── BattleState.java (NEU)
│
└── ui/                       Darstellung & Eingabe
    ├── Drawable.java         (bleibt)
    ├── AsciiImage.java       (erweitert: beliebige Drawables, Farben)
    ├── Renderer.java (NEU)   zeichnet Overworld & Kampfbildschirm
    └── Input.java   (NEU)    Tastatureingabe (siehe Abschnitt 6)
```

> Hinweis: `Animal.java` als Zwischenklasse ist optional. Alternativ
> implementiert `WalkingMammal` direkt das `Battler`-Interface. Entscheidung
> in Phase 1, je nachdem wie sauber sich die Hierarchie anfühlt.

## 4. Kampfsystem im Detail

### 4.1 Werte (Stats)
Jedes Tier hat: `maxHp`, `attack`, `defense`, `speed`, `level`.
Base-Stats pro Tierart (Startwerte, Level 5):

| Tier      | Typ     | HP  | Angr | Vert | Init | Symbol |
|-----------|---------|-----|------|------|------|--------|
| Alpaka    | PFLANZE | 45  | 49   | 49   | 45   | A / 🦙 |
| Pferd     | NORMAL  | 50  | 52   | 43   | 70   | H / 🐴 |
| Elefant   | ERDE    | 80  | 60   | 70   | 25   | E / 🐘 |
| Löwe      | FEUER   | 55  | 70   | 45   | 60   | L / 🦁 |
| Adler     | LUFT    | 40  | 60   | 40   | 80   | D / 🦅 |
| Krokodil  | WASSER  | 60  | 65   | 60   | 35   | K / 🐊 |

### 4.2 Typen-Tabelle (Angreifer → Verteidiger)
Multiplikator: `2.0` = sehr effektiv, `0.5` = nicht sehr effektiv, `1.0` = normal.

```
            ↓ Verteidiger
Angreifer  NORM ERDE WASS FEUR PFLA LUFT
NORMAL      1.0  1.0  1.0  1.0  1.0  1.0
ERDE        1.0  1.0  0.5  2.0  0.5  0.5
WASSER      1.0  2.0  0.5  2.0  0.5  1.0
FEUER       1.0  1.0  0.5  0.5  2.0  1.0
PFLANZE     1.0  2.0  2.0  0.5  0.5  0.5
LUFT        1.0  2.0  1.0  1.0  2.0  1.0
```
(Werte sind ein einfacher, ausbalancierter Anfang – leicht justierbar.)

### 4.3 Attacken (Beispiele)
`Move(name, typ, stärke, genauigkeit, effekt?)`

- Alpaka: **Spucke** (PFLANZE, 40, kann VERGIFTEN), **Tritt** (NORMAL, 35)
- Pferd: **Huftritt** (NORMAL, 45), **Wiehern** (NORMAL, senkt Gegner-Angriff)
- Elefant: **Stampfer** (ERDE, 50), **Rüsselschlag** (NORMAL, 40)
- Löwe: **Flammenbiss** (FEUER, 45), **Brüllen** (NORMAL, senkt Verteidigung)
- Adler: **Sturzflug** (LUFT, 50), **Krallen** (NORMAL, 35)
- Krokodil: **Wasserstoß** (WASSER, 45), **Todesrolle** (WASSER, 55, ungenau)

### 4.4 Schadensformel (vereinfachtes Pokémon)
```
schaden = ( (2*level/5 + 2) * stärke * (angriff/verteidigung) / 50 + 2 )
          * typEffektivität
          * zufall(0.85 .. 1.0)
```
- Vorher Genauigkeits-Wurf: `random < genauigkeit/100` → trifft.
- Mind. 1 Schaden bei Treffer.
- Kampf-Log meldet „sehr effektiv!" / „nicht sehr effektiv …".

### 4.5 Rundenablauf
1. Spieler wählt Attacke aus Menü (oder „Fliehen").
2. Wildes Tier wählt zufällige Attacke.
3. Reihenfolge nach `speed` (höhere Initiative zuerst; bei Gleichstand Zufall).
4. Treffer-Wurf → Schaden → HP abziehen → Effekte anwenden.
5. Statuseffekte am Rundenende (z. B. Gift-Schaden).
6. Prüfen: HP ≤ 0 → besiegt. Sonst nächste Runde.
7. Ende: Sieg / Niederlage / erfolgreiche Flucht → zurück in Overworld.

## 5. Overworld im Detail

- `World` hält ein 2D-Grid aus `Tile` + eine Liste von Tieren (Entities).
- Spieler-Tier wird mit WASD bewegt; Bewegung respektiert Grenzen & Wände
  (behebt den fehlenden Grenz-Check des aktuellen `walk()`).
- Wilde Tiere bewegen sich pro Tick zufällig (vorhandenes `walk()`/`turn()`).
- Kollision = Spielerposition == Tierposition → `BattleState` mit diesem Tier.
- Renderer zeichnet Terrain, dann Tiere darüber, dann eine Status-/Hilfezeile.

## 6. Eingabe & Darstellung (technische Entscheidung)

- **Standard-Eingabe:** Tasten + Enter über `Scanner`/`BufferedReader`.
  Läuft überall (auch in der Eclipse-Konsole), keine Abhängigkeiten.
- **Komfort-Variante (macOS/Linux):** Roh-Modus des Terminals via
  `stty raw -echo` (per `ProcessBuilder`), damit WASD ohne Enter reagiert.
  Wird mit Fallback auf Scanner umgesetzt, falls nicht verfügbar.
- **Bildschirm leeren** pro Frame (ANSI `[2J[H`) für „Animation".
- **Farben/Emoji:** ANSI-Farbcodes; Unicode-/Emoji-Symbole optional
  (per Schalter `useEmoji`, Fallback auf Buchstaben für schmale Terminals).

> Kein Build-System nötig: reines `javac`/Eclipse-Projekt, keine Libraries.

## 7. Umsetzungs-Phasen (Meilensteine)

**Phase 0 – Fundament & Aufräumen**
- `Type`, `Stats`, `Move`, `Status` anlegen.
- `WalkingMammal` um Kampf-Eigenschaften erweitern (oder `Battler`-Interface).
- Tierarten mit Typ/Stats/Attacken ausstatten; neue Arten (Löwe/Adler/Krokodil).
- Bekannte Bugs fixen: `turn()`-Modulo, `Horse`-Shadowing.

**Phase 1 – Overworld**
- `World`, `Tile`, `Player`, `Renderer`, `Input`.
- `Game` + `GameState` + `OverworldState`: Bewegung, Tick, wilde Tiere.
- Kollision erkennt Begegnung (noch ohne Kampf, nur Log).

**Phase 2 – Kampf-Engine**
- `TypeChart`, `DamageCalculator`, `Battle`, `BattleState`.
- Kampf-UI: HP-Balken, Attacken-Menü, Log.
- Vollständiger Kampfloop inkl. Sieg/Niederlage/Flucht und Rückkehr.

**Phase 3 – Polish & Wow**
- ANSI-Farben, Emoji-Symbole, Statuseffekte (Gift/Benommenheit).
- Mehr Tiere/Attacken, Balancing.
- Optionale Extras (siehe unten).

## 8. Optionale Erweiterungen (wenn Zeit bleibt)
- **Team & Fangen**: besiegte Tiere „befreunden" und ins eigene Team aufnehmen.
- **Level & EP**: Siege geben Erfahrung, Werte steigen.
- **Heilung**: Wasserloch betreten heilt HP.
- **Items**: einfache Beeren/Heiltränke.
- **Hitzewelle-Event** (Augenzwinkern der README): senkt zeitweise Initiative.

## 9. Getroffene Entscheidungen
- **Steuerung: Echtzeit-WASD (Raw-Mode)** via `stty raw -echo` auf macOS/Linux,
  mit automatischem Fallback auf Scanner (Taste + Enter), falls nicht verfügbar.
- **Darstellung: Emoji + ANSI-Farben** als Standard (modernes Terminal),
  `useEmoji`-Schalter mit Buchstaben-Fallback bleibt für Notfälle erhalten.
- **Umfang: voller Plan (Phase 0–3)** inkl. Polish und Statuseffekten.
- Noch offen (entscheide ich beim Bau): `Animal`-Zwischenklasse vs.
  `Battler` direkt auf `WalkingMammal`.

## 10. Status
- [x] Phase 0 – Fundament & Aufräumen
  - `Type`, `Stats`, `Move`, `Status`, `MoveEffect`, `Battler` angelegt.
  - `WalkingMammal` implementiert `Battler` direkt (keine `Animal`-Zwischenklasse –
    die Hierarchie bleibt so flach und verständlich).
  - Alle 6 Tierarten mit Typ/Stats/Attacken (Löwe/Adler/Krokodil neu).
  - Bugs gefixt: `turn()`-Modulo sauber normalisiert (0..359), `Horse`-Shadowing
    und Konstruktor-Spam entfernt; Grenz-Check liegt jetzt zentral in `World`.
- [x] Phase 1 – Overworld
  - `World`, `Tile`, `Player`, `Renderer`, `Input` (Raw-Mode + Scanner-Fallback).
  - `Game` + `GameState` + `OverworldState`: WASD-Bewegung, Tick für wilde Tiere,
    Kollision löst Begegnung aus.
- [x] Phase 2 – Kampf-Engine
  - `TypeChart`, `DamageCalculator`, `Battle`, `BattleState`.
  - Kampf-UI: HP-Balken, Attacken-Menü, Kampf-Log; Sieg/Niederlage/Flucht und
    Rückkehr in die Overworld (deterministischer Engine-Test bestanden).
- [x] Phase 3 – Polish & Wow
  - ANSI-Farben, Emoji-Symbole, Statuseffekt Gift (Schaden am Rundenende),
    Stat-Senker (Wiehern/Brüllen), Wasser-Heilung als Bonus, Nachspawnen
    besiegter Tiere.
  - „Juice": schrittweise Runden mit Pausen, animierte HP-Balken, kritische
    Treffer (Volltreffer), Effektivitäts-Hinweis im Attacken-Menü,
    Treffer-Blitz, Intro-Sequenz und Sieg-Bildschirm mit EP-/Siege-Bilanz.
  - Optionale Extras (Team/Fangen, echtes Level-Up, Items) bewusst noch offen.

## 11. Bauen & Spielen
```
javac -d bin -encoding UTF-8 $(find src -name "*.java")
java -cp bin koeln.uni.idh.java1.session11.zoo.Main
```
Steuerung: WASD = bewegen, Q = beenden; im Kampf Ziffern = Attacke, F = fliehen.
Für die Echtzeit-WASD-Steuerung (ohne Enter) muss in einem echten Terminal
gespielt werden; in der Eclipse-Konsole greift der Scanner-Fallback (Taste + Enter).
