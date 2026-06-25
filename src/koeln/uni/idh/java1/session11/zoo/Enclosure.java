package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.Drawable;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;

public class Enclosure implements Drawable {

    private String name;

    private int x;
    private int y;
    private int width;
    private int height;

    private List<WalkingMammal> animals = new ArrayList<>();
    private List<Food> food = new ArrayList<>();

    public Enclosure(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addAnimal(WalkingMammal animal) {
        animals.add(animal);

        animal.setX(x + (int)(Math.random() * width));
        animal.setY(y + (int)(Math.random() * height));
    }

    public void addFood(Food f) {
        food.add(f);
    }

    public List<WalkingMammal> getAnimals() {
        return animals;
    }

    public String getName() {
        return name;
    }

    // 🔁 SIMULATION
    public void tick() {

        for (WalkingMammal a : animals) {

            // 🦁 LÖWEN BREAKOUT CHECK
            if (a instanceof Lion lion) {

                if (lion.triesToEscape()) {
                    System.out.println("🦁 Lion escaped from " + name);

                    // Ziel: zufälliges anderes Gehege
                    // (wird in Simulation abgefangen)
                    a.setX(a.getX() + 5);
                    a.setY(a.getY() + 5);

                    continue;
                }
            }

            // normale Bewegung
            int dir = (int)(Math.random() * 3) - 1;
            a.turn(dir);
            a.walk();

            // 🌾 FOOD CHECK
            Food eaten = null;

            for (Food f : food) {

                if (a.getX() == f.getX() && a.getY() == f.getY()) {

                    a.feed(20);
                    eaten = f;

                    System.out.println(a.getClass().getSimpleName() + " ate food!");
                    break;
                }
            }

            if (eaten != null) {
                food.remove(eaten);
            }
        }

        // ☠️ KOLLISION: Löwe frisst andere Tiere im gleichen Gehege
        for (WalkingMammal a : animals) {

            if (a instanceof Lion) {

                List<WalkingMammal> eatenAnimals = new ArrayList<>();

                for (WalkingMammal target : animals) {

                    if (target != a) {
                        eatenAnimals.add(target);
                        System.out.println("🦁 Lion ate " + target.getClass().getSimpleName());
                    }
                }

                animals.removeAll(eatenAnimals);
                break;
            }
        }
    }

    @Override
    public char getSymbol() {
        return '#'; // Gehege sichtbar machen
    }
}