package koeln.uni.idh.java1.session11.zoo;

import java.util.Scanner;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Giraffe;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;
import koeln.uni.idh.java1.session11.zoo.animals.Penguin;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Zoo zoo = createDemoZoo();

        boolean running = true;

        System.out.println("Willkommen in der Zoo-Simulation!");
        System.out.println("Druecke Enter, um die Simulation zu starten.");

        while (running) {
            printZoo(zoo);

            System.out.println("Aktionen:");
            System.out.println("[Enter] Naechste Runde");
            System.out.println("f       Tier fuettern");
            System.out.println("t       Tier traenken");
            System.out.println("s       Tier streicheln");
            System.out.println("w       Wetter wechseln");
            System.out.println("i       Tierstatus anzeigen");
            System.out.println("l       Legende anzeigen");
            System.out.println("q       Beenden");
            System.out.print("Eingabe: ");

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("")) {
                zoo.simulateStep();
            } else if (input.equals("f")) {
                feedAnimal(scanner, zoo);
            } else if (input.equals("t")) {
                giveWaterToAnimal(scanner, zoo);
            } else if (input.equals("s")) {
                petAnimal(scanner, zoo);
            } else if (input.equals("w")) {
                zoo.cycleWeather();
            } else if (input.equals("i")) {
                System.out.println();
                System.out.println(zoo.getAnimalList());
                waitForEnter(scanner);
            } else if (input.equals("l")) {
                System.out.println();
                System.out.println(zoo.getLegend());
                waitForEnter(scanner);
            } else if (input.equals("q")) {
                running = false;
            } else {
                System.out.println("Unbekannte Eingabe.");
                waitForEnter(scanner);
            }
        }

        scanner.close();

        System.out.println("Simulation beendet.");
    }

    private static Zoo createDemoZoo() {
        Zoo zoo = new Zoo(15, 10);

        zoo.addAnimal(new Alpaca("Alfred", 1, 1));
        zoo.addAnimal(new Elephant("Emma", 5, 4));
        zoo.addAnimal(new Horse("Hektor", 3, 7, "braun"));
        zoo.addAnimal(new Giraffe("Gloria", 10, 2));
        zoo.addAnimal(new Penguin("Paul", 12, 8));
        zoo.addAnimal(new Lion("Leo", 8, 6));

        zoo.addTree(new Tree(2, 2));
        zoo.addTree(new Tree(11, 1));
        zoo.addTree(new Tree(13, 6));

        zoo.addWater(new Water(0, 8));
        zoo.addWater(new Water(12, 7));

        zoo.addFoodStation(new FoodStation(6, 2));
        zoo.addFoodStation(new FoodStation(4, 8));

        zoo.addRock(new Rock(7, 5));
        zoo.addRock(new Rock(9, 5));
        zoo.addRock(new Rock(1, 6));

        return zoo;
    }

    private static void printZoo(Zoo zoo) {
        System.out.println();
        System.out.println("=================================");
        System.out.println(zoo.getStatus());
        System.out.println(zoo.draw());
        System.out.println("=================================");
    }

    private static void feedAnimal(Scanner scanner, Zoo zoo) {
        System.out.println();
        System.out.println("Welches Tier soll gefuettert werden?");
        System.out.println(zoo.getAnimalList());

        int index = readAnimalNumber(scanner);

        if (index >= 0) {
            zoo.feedAnimal(index);
        }

        waitForEnter(scanner);
    }

    private static void giveWaterToAnimal(Scanner scanner, Zoo zoo) {
        System.out.println();
        System.out.println("Welches Tier soll Wasser bekommen?");
        System.out.println(zoo.getAnimalList());

        int index = readAnimalNumber(scanner);

        if (index >= 0) {
            zoo.giveWaterToAnimal(index);
        }

        waitForEnter(scanner);
    }

    private static void petAnimal(Scanner scanner, Zoo zoo) {
        System.out.println();
        System.out.println("Welches Tier soll gestreichelt werden?");
        System.out.println(zoo.getAnimalList());

        int index = readAnimalNumber(scanner);

        if (index >= 0) {
            zoo.petAnimal(index);
        }

        waitForEnter(scanner);
    }

    private static int readAnimalNumber(Scanner scanner) {
        System.out.print("Nummer eingeben: ");

        String input = scanner.nextLine().trim();

        try {
            int number = Integer.parseInt(input);
            return number;
        } catch (NumberFormatException e) {
            System.out.println("Das war keine gueltige Zahl.");
            return -1;
        }
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.println();
        System.out.print("Weiter mit Enter...");
        scanner.nextLine();
    }
}