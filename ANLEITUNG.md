# Zookémon – Kurzanleitung 🦙⚔️

## Worum geht's?
Du steuerst ein Tier durch ein ASCII-Gehege. Wilde Tiere laufen frei umher –
läufst du in eines hinein, startet ein **rundenbasierter Pokémon-Kampf**: Jedes
Tier hat HP, Werte, einen Typ und Attacken. Typ-Effektivität und etwas Zufall
entscheiden über den Schaden. Wer zuerst auf 0 HP fällt, verliert.

## Starten
```
javac -d bin -encoding UTF-8 $(find src -name "*.java")
java -cp bin koeln.uni.idh.java1.session11.zoo.Main
```
Am besten in einem echten Terminal (WASD ohne Enter). In der Eclipse-Konsole
funktioniert es auch – dort jeweils **Taste + Enter** drücken.

## Steuerung
**Overworld**
- `W` / `A` / `S` / `D` – bewegen
- `Q` – beenden
- Wasserfelder (`~`) heilen ein bisschen.

**Im Kampf**
- `1`, `2`, … – Attacke wählen
- `F` – fliehen

## Tipp
Achte auf den Typ! Z. B. ist Pflanze gegen Wasser sehr effektiv, Wasser gegen
Pflanze schwach. Der Kampf-Log meldet „sehr effektiv!" bzw. „nicht sehr
effektiv …".
