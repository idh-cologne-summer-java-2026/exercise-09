package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

    static List<WalkingMammal> animals = new ArrayList<>();
    static AsciiImage ai = new AsciiImage(10, 10);
    static Random random = new Random();
    static int lastBlitzX = -1;
    static int lastBlitzY = -1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Willkommen im Zoo!");
        System.out.println("Befehle: spawn alpaca | spawn horse | spawn elephant | move | blitz | quit");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("quit")) {
                System.out.println("Tschüss!");
                break;

            } else if (input.startsWith("spawn")) {
                String type = input.replace("spawn", "").trim();
                spawnAnimal(type);

            } else if (input.equals("move")) {
                moveAllAnimals();
                checkBreeding();

            }  else if (input.equals("blitz")) {
                blitzEvent();
            }
            else {
                System.out.println("Unbekannter Befehl.");
            }

            printImage();
        }

        scanner.close();
    }

    static void spawnAnimal(String type) {
        WalkingMammal animal = null;

        switch (type) {
            case "alpaca":
                animal = new Alpaca();
                break;
            case "horse":
                animal = new Horse("Horse", "brown");
                break;
            case "elephant":
                animal = new Elephant();
                break;
            default:
                System.out.println("Unbekanntes Tier: " + type);
                return;
        }

        // Zufällige Startposition
        animal.setX(random.nextInt(10));
        animal.setY(random.nextInt(10));
        animals.add(animal);
        System.out.println(type + " gespawnt bei (" + animal.getX() + "," + animal.getY() + ")");
    }

    static void moveAllAnimals() {
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        for (WalkingMammal animal : animals) {
            int dir = random.nextInt(4);
            int newX = animal.getX() + dx[dir];
            int newY = animal.getY() + dy[dir];

            // Innerhalb des Rasters bleiben
            newX = Math.max(0, Math.min(9, newX));
            newY = Math.max(0, Math.min(9, newY));

            animal.setX(newX);
            animal.setY(newY);
        }
    }

    static void checkBreeding() {
        List<WalkingMammal> newAnimals = new ArrayList<>();

        for (int i = 0; i < animals.size(); i++) {
            for (int j = i + 1; j < animals.size(); j++) {
                WalkingMammal a = animals.get(i);
                WalkingMammal b = animals.get(j);

                // Gleiche Position und gleicher Typ?
                if (a.getX() == b.getX() && a.getY() == b.getY()
                        && a.getClass() == b.getClass()) {

                    System.out.println("Zwei " + a.getClass().getSimpleName() 
                        + " auf (" + a.getX() + "," + a.getY() + ") — neues Tier wird geboren!");

                    try {
                        WalkingMammal baby = a.getClass().getDeclaredConstructor().newInstance();
                        baby.setX(a.getX());
                        baby.setY(a.getY());
                        newAnimals.add(baby);
                    } catch (Exception e) {
                        System.out.println("Fehler beim Erstellen des Babys: " + e.getMessage());
                    }
                }
            }
        }

        animals.addAll(newAnimals);
    }

    static void blitzEvent() {
        // Zufällige Einschlagsposition (so dass 3x3 ins Raster passt)
        int centerX = 1 + random.nextInt(8); // 1-8 damit 3x3 nicht rausgeht
        int centerY = 1 + random.nextInt(8);

        System.out.println("⚡ BLITZ schlägt ein bei (" + centerX + "," + centerY + ")!");

        List<WalkingMammal> survivors = new ArrayList<>();

        for (WalkingMammal animal : animals) {
            int ax = animal.getX();
            int ay = animal.getY();

            // Liegt das Tier im 3x3 Bereich?
            if (ax >= centerX - 1 && ax <= centerX + 1
                    && ay >= centerY - 1 && ay <= centerY + 1) {
                System.out.println(animal.getClass().getSimpleName() 
                    + " bei (" + ax + "," + ay + ") wurde vom Blitz getroffen und stirbt!");
            } else {
                survivors.add(animal);
            }
            lastBlitzX = centerX;
            lastBlitzY = centerY;
        }

        animals = survivors;
    }
    static void printImage() {
        // Bild zurücksetzen
        AsciiImage image = new AsciiImage(10, 10);
        
     // Blitzbereich anzeigen
        if (lastBlitzX >= 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int bx = lastBlitzX + dx;
                    int by = lastBlitzY + dy;
                    if (bx >= 0 && bx < 10 && by >= 0 && by < 10) {
                        image.dot(bx, by, '*'); // '*' für Blitzbereich
                    }
                }
            }
            lastBlitzX = -1; // Nach einem Zug wieder zurücksetzen
            lastBlitzY = -1;
        }

        for (WalkingMammal animal : animals) {
            image.dot(animal.getX(), animal.getY(), animal);
        }

        System.out.println(image.toString());
    }
}