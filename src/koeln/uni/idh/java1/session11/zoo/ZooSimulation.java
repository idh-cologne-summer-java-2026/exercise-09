package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;

public class ZooSimulation {

    private List<Enclosure> enclosures = new ArrayList<>();

    private boolean running = true;
    private int tickCount = 0;

    // 🏗️ Gehege hinzufügen
    public void addEnclosure(Enclosure e) {
        enclosures.add(e);
    }

    // 🔁 EIN SIMULATIONS-STEP
    public void tick() {

        System.out.println("===== TICK " + tickCount + " =====");

        for (Enclosure e : enclosures) {
            e.tick();
        }

        handleDeaths();
        handleLionTransfers();
        checkEndCondition();

        tickCount++;
    }

    // ☠️ Hunger-Tod prüfen
    private void handleDeaths() {

        for (Enclosure e : enclosures) {

            List<WalkingMammal> dead = new ArrayList<>();

            for (WalkingMammal a : e.getAnimals()) {

                if (a.isStarving()) {
                    dead.add(a);
                }
            }

            e.getAnimals().removeAll(dead);

            if (!dead.isEmpty()) {
                System.out.println("Deaths in " + e.getName() + ": " + dead.size());
            }
        }
    }

    // 🏁 Endbedingung: nur Löwen übrig
    private void checkEndCondition() {

        int nonLions = 0;

        for (Enclosure e : enclosures) {
            for (WalkingMammal a : e.getAnimals()) {
                if (!a.getClass().getSimpleName().equals("Lion")) {
                    nonLions++;
                }
            }
        }

        if (nonLions == 0) {
            running = false;
            System.out.println("🏁 GAME OVER: Only lions remain!");
        }
    }

    public boolean isRunning() {
        return running;
    }
    
    private void handleLionTransfers() {

        for (Enclosure from : enclosures) {

            for (WalkingMammal a : new ArrayList<>(from.getAnimals())) {

                if (a instanceof Lion) {

                    // zufälliges anderes Gehege
                    for (Enclosure to : enclosures) {

                        if (to != from && Math.random() < 0.2) {

                            from.getAnimals().remove(a);
                            to.addAnimal(a);

                            System.out.println("🦁 Lion moved from " +
                                    from.getName() + " to " + to.getName());

                            return;
                        }
                    }
                }
            }
        }
    }
}