# Zookémon – Kurzanleitung 🦙⚔️

## Worum geht's?
Zu Beginn wählst du – wie im echten Spiel – eines von **drei Starter-Zookémon**
(Flauschpaka 🌱, Glutprankel 🔥 oder Schnappix 💧). Damit steuerst du ein Tier
durch ein ASCII-Gehege. Wilde Tiere laufen frei umher –
läufst du in eines hinein, startet ein **rundenbasierter Pokémon-Kampf**: Jedes
Tier hat HP, Werte, einen Typ und Attacken. Typ-Effektivität und etwas Zufall
entscheiden über den Schaden. Wer zuerst auf 0 HP fällt, verliert.

**Story & Ziel:** Prof. Nils hat dich in seiner **ASCII-Welt** gefangen. Er lässt
dich erst frei, wenn du deine Java-Fähigkeiten so weit trainiert hast, dass du
ihn besiegen und diese **Java-Welt zerstören** kannst. Er hat sich genau das
Zookémon geschnappt, das gegen dein Starter-Zookémon stark ist. Werde durch
Kämpfe und Fangen stärker, bis du ihn in seiner Festung (Symbol `N`) schlägst –
dann zerfällt die Welt und du bist frei!

## Starten
```
javac -d bin -encoding UTF-8 $(find src -name "*.java")
java -cp bin koeln.uni.idh.java1.session11.zoo.Main
```
Am besten in einem echten Terminal (WASD ohne Enter). In der Eclipse-Konsole
funktioniert es auch – dort jeweils **Taste + Enter** drücken.

## Steuerung
**Titelbildschirm**
- Beim Start fliegt das große „ZOOKEMON"-Logo herein – eine beliebige Taste
  beginnt das Abenteuer.

**Overworld**
- `W` / `A` / `S` / `D` – bewegen
- `M` – Ton an/aus (Treffer, Level-Aufstieg, Sieg und Nils' Auftritt sind mit
  dem Terminal-Signalton untermalt)
- `Q` – beenden
- Wasserfelder (`~`) heilen ein bisschen.
- Die **Farbe** eines wilden Tieres verrät sein Level: weiß (0–9), cyan
  (10–19), gelb (20–29), rot (30+). So siehst du schon auf der Karte, wo
  starke Gegner lauern. Dieselbe Skala färbt die „Lv X"-Zahl im Kampf.
  Je höher dein eigenes Level, desto häufiger spawnen auch stärkere Tiere.

**Im Kampf**
- `1`, `2`, … – Attacke wählen
- `Z` – **fangen** (wildes Tier ins Team holen)
- `W` – **Tier wechseln** (kostet einen Zug)
- `F` – fliehen

## Team & Fangen
Du kannst bis zu **6 Tiere** in deinem Team haben. Schwäche ein wildes Tier im
Kampf und wirf ihm dann mit `Z` ein Leckerli zu – je weniger HP es noch hat (und
wenn es vergiftet ist), desto eher schließt es sich dir an. Mit `W` wechselst du
mitten im Kampf das aktive Tier. Wird dein aktives Tier besiegt, schickst du
einfach ein anderes in den Kampf – verloren hast du erst, wenn **alle** Tiere
besiegt sind.

## Verbündete (NPCs)
Nicht alles in der Welt ist ein Zookémon: Zwei **Verbündete** stehen an festen
Orten und sind an ihren Sonderzeichen zu erkennen (keine Tier-Buchstaben).
Lauf gegen sie, um mit ihnen zu reden – beim **ersten Gespräch** geben sie dir
einen dauerhaften Buff fürs ganze Team (auch für später gefangene Tiere).
- `&` **Kommilitone** (im Baum-Hain): hat Nils' ASCII-Aufgaben ebenfalls satt und
  teilt seine Lösungs-Notizen → **+Angriff**.
- `?` **K.I. »GPT-Zoo«** (am Wasser-Teich): wird ständig für Hausaufgaben
  missbraucht und will Nils stürzen → optimiert dein Team: **+Verteidigung &
  Initiative**.

## Prof. Nils, der Erzfeind
Nils steht an einem festen Ort (`N`). Lauf gegen ihn, um ihn herauszufordern –
vor dem Kampf wird dein Team **voll geheilt**. Sein Zookémon ist **Level 50**,
genau vom **Konter-Typ** deines Starters und entwickelt – du brauchst also ein
starkes Team. Fangen kannst du es nicht, aber **fliehen ist erlaubt** (`F`),
falls es brenzlig wird. Forderst du ihn erneut heraus, hat er wieder **volle
HP**. Sein Auftritt wird groß inszeniert – und fällt sein Zookémon unter die
Hälfte seiner HP, dreht es in **Phase 2** vor Wut auf (höherer Angriff).
**Tipp:** Fang dir gezielt ein Tier, dessen Typ *seinen* Typ kontert!
Verlierst du, wird dein Team geheilt und du landest wieder in der Welt – einfach
weitertrainieren und es erneut versuchen.

## Tipp
Achte auf den Typ! Z. B. ist Pflanze gegen Wasser sehr effektiv, Wasser gegen
Pflanze schwach. Der Kampf-Log meldet „sehr effektiv!" bzw. „nicht sehr
effektiv …".
