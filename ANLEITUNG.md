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
**Overworld**
- `W` / `A` / `S` / `D` – bewegen
- `Q` – beenden
- Wasserfelder (`~`) heilen ein bisschen.

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

## Prof. Nils, der Erzfeind
Nils steht an einem festen Ort (`N`). Lauf gegen ihn, um ihn herauszufordern –
vor dem Kampf wird dein Team **voll geheilt**, du kämpfst aber nicht fangbar und
ohne Fluchtmöglichkeit. Sein Zookémon ist genau vom **Konter-Typ** deines
Starters und entwickelt sowie hochstufig – du brauchst also ein starkes Team.
**Tipp:** Fang dir gezielt ein Tier, dessen Typ *seinen* Typ kontert! Verlierst
du, wird dein Team geheilt und du landest wieder in der Welt – einfach
weitertrainieren und es erneut versuchen.

## Tipp
Achte auf den Typ! Z. B. ist Pflanze gegen Wasser sehr effektiv, Wasser gegen
Pflanze schwach. Der Kampf-Log meldet „sehr effektiv!" bzw. „nicht sehr
effektiv …".
