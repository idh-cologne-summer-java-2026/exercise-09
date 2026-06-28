package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;

import koeln.uni.idh.java1.session11.zoo.animals.Giraffe;
import koeln.uni.idh.java1.session11.zoo.animals.Penguin;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Zoo {

    private final int width;
    private final int height;

    private int round;
    private Weather weather;

    private final List<WalkingMammal> animals;
    private final List<Tree> trees;
    private final List<Water> waters;
    private final List<FoodStation> foodStations;
    private final List<Rock> rocks;

    public Zoo(int width, int height) {
        this.width = width;
        this.height = height;

        this.round = 0;
        this.weather = Weather.NORMAL;

        this.animals = new ArrayList<>();
        this.trees = new ArrayList<>();
        this.waters = new ArrayList<>();
        this.foodStations = new ArrayList<>();
        this.rocks = new ArrayList<>();
    }

    public void addAnimal(WalkingMammal animal) {
        if (animal != null) {
            animals.add(animal);
        }
    }

    public void addTree(Tree tree) {
        if (tree != null) {
            trees.add(tree);
        }
    }

    public void addWater(Water water) {
        if (water != null) {
            waters.add(water);
        }
    }

    public void addFoodStation(FoodStation foodStation) {
        if (foodStation != null) {
            foodStations.add(foodStation);
        }
    }

    public void addRock(Rock rock) {
        if (rock != null) {
            rocks.add(rock);
        }
    }

    public void simulateStep() {
        round++;

        for (WalkingMammal animal : animals) {
            animal.tick(weather);
            animal.walk(width, height);
            handleEnvironment(animal);
        }

        handleAnimalInteractions();
    }

    private void handleEnvironment(WalkingMammal animal) {
        if (!animal.isAlive()) {
            return;
        }

        handleFoodStations(animal);
        handleWater(animal);
        handleTrees(animal);
    }

    private void handleFoodStations(WalkingMammal animal) {
        for (FoodStation foodStation : foodStations) {
            if (animal.isNear(foodStation.getX(), foodStation.getY()) && animal.isHungry()) {
                animal.eat();
                System.out.println(animal.getName() + " frisst an einer Futterstation.");
                return;
            }
        }
    }

    private void handleWater(WalkingMammal animal) {
        for (Water water : waters) {
            if (animal.isNear(water.getX(), water.getY()) && animal.isThirsty()) {
                if (animal instanceof Penguin) {
                    Penguin penguin = (Penguin) animal;
                    penguin.coolDown();
                } else {
                    animal.drink();
                    System.out.println(animal.getName() + " trinkt an einer Wasserstelle.");
                }

                return;
            }
        }
    }

    private void handleTrees(WalkingMammal animal) {
        if (!(animal instanceof Giraffe)) {
            return;
        }

        Giraffe giraffe = (Giraffe) animal;

        for (Tree tree : trees) {
            if (giraffe.isNear(tree.getX(), tree.getY()) && giraffe.isHungry()) {
                giraffe.eatLeaves();
                return;
            }
        }
    }

    private void handleAnimalInteractions() {
        for (int i = 0; i < animals.size(); i++) {
            WalkingMammal first = animals.get(i);

            for (int j = i + 1; j < animals.size(); j++) {
                WalkingMammal second = animals.get(j);

                if (first.isAt(second.getX(), second.getY())) {
                    first.interact(second);
                    second.interact(first);
                }
            }
        }
    }

    public AsciiImage draw() {
        AsciiImage image = new AsciiImage(width, height);

        for (Tree tree : trees) {
            image.dot(tree.getX(), tree.getY(), tree);
        }

        for (Water water : waters) {
            image.dot(water.getX(), water.getY(), water);
        }

        for (FoodStation foodStation : foodStations) {
            image.dot(foodStation.getX(), foodStation.getY(), foodStation);
        }

        for (Rock rock : rocks) {
            image.dot(rock.getX(), rock.getY(), rock);
        }

        for (WalkingMammal animal : animals) {
            if (animal.isAlive()) {
                image.dot(animal.getX(), animal.getY(), animal);
            } else {
                image.setPixel(animal.getX(), animal.getY(), 'x');
            }
        }

        return image;
    }

    public void feedAnimal(int index) {
        WalkingMammal animal = getAnimalByIndex(index);

        if (animal == null) {
            System.out.println("Kein Tier mit dieser Nummer gefunden.");
            return;
        }

        if (!animal.isAlive()) {
            System.out.println(animal.getName() + " kann nicht mehr gefüttert werden.");
            return;
        }

        animal.eat();
        System.out.println(animal.getName() + " wurde gefüttert.");
    }

    public void giveWaterToAnimal(int index) {
        WalkingMammal animal = getAnimalByIndex(index);

        if (animal == null) {
            System.out.println("Kein Tier mit dieser Nummer gefunden.");
            return;
        }

        if (!animal.isAlive()) {
            System.out.println(animal.getName() + " kann nicht mehr trinken.");
            return;
        }

        animal.drink();
        System.out.println(animal.getName() + " hat Wasser bekommen.");
    }

    public void petAnimal(int index) {
        WalkingMammal animal = getAnimalByIndex(index);

        if (animal == null) {
            System.out.println("Kein Tier mit dieser Nummer gefunden.");
            return;
        }

        if (!animal.isAlive()) {
            System.out.println(animal.getName() + " kann nicht mehr gestreichelt werden.");
            return;
        }

        animal.pet();
        System.out.println(animal.getName() + " wurde gestreichelt.");
    }

    private WalkingMammal getAnimalByIndex(int index) {
        if (index < 0 || index >= animals.size()) {
            return null;
        }

        return animals.get(index);
    }

    public void cycleWeather() {
        if (weather == Weather.NORMAL) {
            weather = Weather.HEATWAVE;
        } else if (weather == Weather.HEATWAVE) {
            weather = Weather.RAIN;
        } else {
            weather = Weather.NORMAL;
        }

        System.out.println("Das Wetter wechselt zu: " + weather);
    }

    public void setWeather(Weather weather) {
        if (weather != null) {
            this.weather = weather;
        }
    }

    public String getAnimalList() {
        String result = "";

        for (int i = 0; i < animals.size(); i++) {
            WalkingMammal animal = animals.get(i);
            result += i + ": " + animal.getStatus() + "\n";
        }

        return result;
    }

    public String getStatus() {
        String result = "";

        result += "Runde: " + round + "\n";
        result += "Wetter: " + weather + "\n";
        result += "Tiere im Zoo: " + animals.size() + "\n";

        int livingAnimals = 0;

        for (WalkingMammal animal : animals) {
            if (animal.isAlive()) {
                livingAnimals++;
            }
        }

        result += "Aktive Tiere: " + livingAnimals + "\n";

        return result;
    }

    public String getLegend() {
        return "Legende:\n"
                + "A = Alpaka\n"
                + "E = Elefant\n"
                + "G = Giraffe\n"
                + "H = Pferd\n"
                + "L = Löwe\n"
                + "P = Pinguin\n"
                + "T = Baum\n"
                + "~ = Wasser\n"
                + "F = Futterstation\n"
                + "# = Stein\n"
                + "x = nicht mehr aktives Tier\n";
    }

    public List<WalkingMammal> getAnimals() {
        return animals;
    }

    public int getRound() {
        return round;
    }

    public Weather getWeather() {
        return weather;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}