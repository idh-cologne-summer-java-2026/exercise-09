package main.koeln.uni.idh.java1.session11.zoo.animals;

import main.koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public abstract class WalkingMammal implements Drawable {
    // 1. Basis-Attribute für Position und Name
    String name;
    int x = 1;
    int y = 1;
    int stepsize = 1;
    int direction = 0;

    // 2. Tamagotchi-Attribute (Werte von 0 bis 100)
    private int hunger = 80;      // Start-Sättigung
    private int happiness = 80;   // Start-Zufriedenheit
    private int energy = 80;      // Start-Energie

    // Konstruktor
    public WalkingMammal() {
        this.name = this.getClass().getSimpleName(); // Setzt den Namen auf z.B. "Alpaca"
    }

    // 3. Interaktionen für den Pflege-Tab (Füttern, Spielen, Schlafen)
    public void feed() {
        this.hunger = Math.min(100, this.hunger + 30);
        this.happiness = Math.min(100, this.happiness + 5);
        System.out.println(name + " wurde gefüttert.");
    }

    public void play() {
        if (this.energy > 15) {
            this.happiness = Math.min(100, this.happiness + 25);
            this.energy = Math.max(0, this.energy - 15); // Spielen kostet Energie
            System.out.println("Du hast mit " + name + " gespielt.");
        } else {
            System.out.println(name + " ist zu müde zum Spielen!");
        }
    }

    public void sleep() {
        this.energy = Math.min(100, this.energy + 40);
        this.hunger = Math.max(0, this.hunger - 10); // Schlafen kostet etwas Hunger
        System.out.println(name + " hat geschlafen und tankt Energie.");
    }

    // 4. Zeit-Simulation (Wird jede Sekunde im Game-Loop aufgerufen)
    public void passTime() {
        // Werte sinken langsam im Sekundentakt
        this.hunger = Math.max(0, this.hunger - 2);
        this.energy = Math.max(0, this.energy - 1);

        // Wenn hungrig oder müde, sinkt die Laune deutlich schneller
        if (this.hunger < 30 || this.energy < 30) {
            this.happiness = Math.max(0, this.happiness - 3);
        } else {
            this.happiness = Math.max(0, this.happiness - 1);
        }

        // Zufällige Bewegung im Gehege (30% Chance zu laufen, 20% Chance sich zu drehen)
        double rand = Math.random();
        if (rand < 0.3) {
            this.walk();
        } else if (rand < 0.5) {
            this.turn(Math.random() > 0.5 ? 1 : -1);
        }
    }

    // 5. Bewegungsmethoden
    public void walk() {
        switch (direction) {
            case 0: this.y = this.y - stepsize; break;   // Oben
            case 90: this.x = this.x + stepsize; break;  // Rechts
            case 180: this.y = this.y + stepsize; break; // Unten
            case 270: this.x = this.x - stepsize; break; // Links
        }
        this.energy = Math.max(0, this.energy - 2); // Laufen kostet zusätzlich Energie
    }

    public void turn(int turnDirection) {
        this.direction = (int) (this.direction + (Math.signum(turnDirection) * 90) % 360);
    }

    // 6. Alle Getter-Methoden für das UI-System (Main.java)
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getDirection() { return this.direction; }
    public String getName() { return this.name != null ? this.name : getClass().getSimpleName(); }
    
    public int getHunger() { return this.hunger; }
    public int getHappiness() { return this.happiness; }
    public int getEnergy() { return this.energy; }
}