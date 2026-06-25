package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.*;

public class Main {

    public static void main(String[] args) {

        ZooSimulation sim = new ZooSimulation();

        // 🏞️ GEHEGE ERSTELLEN
        Enclosure savanna = new Enclosure("Savanna", 0, 0, 10, 10);
        Enclosure monkeyHouse = new Enclosure("Monkey House", 10, 0, 10, 10);
        Enclosure lionDen = new Enclosure("Lion Den", 0, 10, 10, 10);

        // 🐾 SAVANNA TIERE
        savanna.addAnimal(new Giraffe());
        savanna.addAnimal(new Zebra());
        savanna.addAnimal(new Elephant());
        savanna.addAnimal(new Antelope());

        // 🐒 AFFEN
        monkeyHouse.addAnimal(new Monkey());

        // 🦁 LÖWEN
        lionDen.addAnimal(new Lion());

        // 🌾 FOOD IN SAVANNA
        savanna.addFood(new Food(3, 3));
        savanna.addFood(new Food(6, 6));
        savanna.addFood(new Food(8, 2));

        // 🌾 FOOD IN AFFENGEHEGE
        monkeyHouse.addFood(new Food(12, 2));
        monkeyHouse.addFood(new Food(15, 5));

        // 🌾 FOOD BEI LÖWEN (optional)
        lionDen.addFood(new Food(1, 11));
        lionDen.addFood(new Food(4, 12));

        // 🏗️ GEHEGE ZUR SIMULATION HINZUFÜGEN
        sim.addEnclosure(savanna);
        sim.addEnclosure(monkeyHouse);
        sim.addEnclosure(lionDen);

        // 🔁 GAME LOOP
        while (sim.isRunning()) {
            sim.tick();

            try {
                Thread.sleep(500); // Geschwindigkeit der Simulation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Simulation beendet.");
    }
}