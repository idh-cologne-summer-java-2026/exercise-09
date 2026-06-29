public static void main(String[] args) {

    WalkingMammal wm = new Alpaca();
    AsciiImage ai = new AsciiImage(10, 10);

    ai.dot(5, 5, wm);

    System.out.println(ai.toString());

    // Simulation über 5 Runden
    for (int i = 1; i <= 5; i++) {

        System.out.println("\nRunde " + i);

        wm.walk();

        System.out.println("Hunger: " + wm.getHunger());
        System.out.println("Durst: " + wm.getThirst());

        // Nach der 3. Runde werden die Tiere gefüttert
        if (i == 3) {
            System.out.println("Fütterungszeit!");

            wm.feed();
            wm.drink();

            System.out.println("Das Tier wurde gefüttert und hat getrunken.");
        }
    }

    System.out.println("\nEndstand:");
    System.out.println("Hunger: " + wm.getHunger());
    System.out.println("Durst: " + wm.getThirst());
}