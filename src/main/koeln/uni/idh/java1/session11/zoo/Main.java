package main.koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.koeln.uni.idh.java1.session11.zoo.animals.*;

public class Main extends Application {

    // Spiel-Variablen (Wirtschaftssystem)
    private int money = 100;       // Startkapital
    private int foodStock = 5;     // Startfutter im Inventar

    private List<WalkingMammal> zooPets = new ArrayList<>();
    private WalkingMammal currentPet; // Für den Pflege-Tab
    
    private final int GRID_SIZE = 10; // Gehege-Größe (10x10)

    // UI Elemente (Globale Updates)
    private Label moneyLabel = new Label();
    private Label foodLabel = new Label();
    private Label careNameLabel = new Label();
    private ProgressBar hungerBar = new ProgressBar();
    private ProgressBar happinessBar = new ProgressBar();
    private ProgressBar energyBar = new ProgressBar();
    private ComboBox<String> petSelector = new ComboBox<>();
    private GridPane enclosureGrid = new GridPane(); // Das visuelle Gehege

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Zoo-Tycoon & Tamagotchi Manager");

        // 1. Start-Tiere erzeugen
        zooPets.add(new Alpaca());
        zooPets.add(new Elephant());
        zooPets.add(new Horse("Blitz", "Braun"));
        currentPet = zooPets.get(0);

        // Top-Leiste für Geld und Futter (Immer sichtbar)
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #2c3e50;");
        moneyLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        foodLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        topBar.getChildren().addAll(moneyLabel, foodLabel);

        // 2. TABS ERSTELLEN
        TabPane tabPane = new TabPane();
        
        Tab tabGehege = new Tab("🌐 Gehege", createGehegeView());
        Tab tabPflege = new Tab("❤️ Tierpflege", createPflegeView());
        Tab tabShop = new Tab("🛒 Zoo-Shop", createShopView());
        
        // Verhindern, dass man Tabs schließen kann
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(tabGehege, tabPflege, tabShop);

        // Haupt-Layout
        VBox root = new VBox();
        root.getChildren().addAll(topBar, tabPane);

        // --- GAME LOOP (Timer für Bewegung, Alterung & Wirtschaft) ---
        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            int totalIncome = 0;

            for (WalkingMammal pet : zooPets) {
                pet.passTime(); // Tiere werden hungriger/müder, bewegen sich zufällig

                // Besucher-Logik: Ein glückliches Tier bringt Geld!
                if (pet.getHunger() > 20 && pet.getHappiness() > 20) {
                    totalIncome += 5; // 5 Gold pro glücklichem Tier
                }
            }

            money += totalIncome;

            // UI erneuern
            updateUI();
            renderGehege(); // Gehege neu zeichnen, da Tiere gelaufen sind
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        // Initiales Zeichnen
        updateUI();
        renderGehege();

        Scene scene = new Scene(root, 450, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- TAB 1: DAS GEHEGE (VISUELL) ---
    private VBox createGehegeView() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setAlignment(Pos.CENTER);
        
        enclosureGrid.setAlignment(Pos.CENTER);
        box.getChildren().addAll(new Label("Dein Gehege (Live-Ansicht):"), enclosureGrid);
        return box;
    }

    private void renderGehege() {
        enclosureGrid.getChildren().clear(); // Altes Raster löschen

        // 10x10 Raster aufbauen
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                StackPane cell = new StackPane();
                
                // Rasen-Hintergrund setzen
                ImageView grassView = loadSprite("grass");
                grassView.setFitWidth(40);
                grassView.setFitHeight(40);
                cell.getChildren().add(grassView);

                // Prüfen, ob hier ein Tier steht
                for (WalkingMammal pet : zooPets) {
                    // Modulo sorgt dafür, dass die Tiere innerhalb des 10x10 Grids bleiben
                    int petX = Math.abs(pet.getX() % GRID_SIZE);
                    int petY = Math.abs(pet.getY() % GRID_SIZE);

                    if (petX == x && petY == y) {
                        // Dynamischer Bildname basierend auf Richtung, z.B. "Alpaca_down"
                        String directionSuffix = getDirectionSuffix(pet.getDirection());
                        String spriteName = pet.getClass().getSimpleName() + directionSuffix;
                        
                        ImageView petView = loadSprite(spriteName);
                        petView.setFitWidth(35);
                        petView.setFitHeight(35);
                        cell.getChildren().add(petView); // Tier über den Rasen legen
                    }
                }
                enclosureGrid.add(cell, x, y);
            }
        }
    }

    // --- TAB 2: TIERPFLEGE (TAMAGOTCHI) ---
    private VBox createPflegeView() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);

        // Dropdown befüllen
        for (WalkingMammal p : zooPets) { petSelector.getItems().add(p.getName()); }
        petSelector.getSelectionModel().select(0);
        petSelector.setOnAction(e -> {
            int idx = petSelector.getSelectionModel().getSelectedIndex();
            if (idx >= 0) { currentPet = zooPets.get(idx); updateUI(); }
        });

        // Balken
        hungerBar.setPrefWidth(200);
        happinessBar.setPrefWidth(200);
        energyBar.setPrefWidth(200);

        VBox stats = new VBox(5, 
            new Label("Sättigung:"), hungerBar, 
            new Label("Zufriedenheit:"), happinessBar, 
            new Label("Energie:"), energyBar
        );
        stats.setAlignment(Pos.CENTER_LEFT);
        stats.setPadding(new Insets(0, 0, 0, 100));

        // Interaktionen
        Button feedBtn = new Button("🍖 Füttern (-1 Futter)");
        Button playBtn = new Button("⚽ Spielen");
        Button sleepBtn = new Button("💤 Schlafen");

        feedBtn.setOnAction(e -> {
            if (foodStock > 0) {
                foodStock--;
                currentPet.feed();
                updateUI();
            } else {
                showAlert("Kein Futter!", "Du musst erst Futter im Shop kaufen.");
            }
        });
        playBtn.setOnAction(e -> { currentPet.play(); updateUI(); });
        sleepBtn.setOnAction(e -> { currentPet.sleep(); updateUI(); });

        HBox actions = new HBox(10, feedBtn, playBtn, sleepBtn);
        actions.setAlignment(Pos.CENTER);

        box.getChildren().addAll(new Label("Wähle Tier:"), petSelector, careNameLabel, stats, actions);
        return box;
    }

    // --- TAB 3: ZOO-SHOP ---
    private VBox createShopView() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);

        Button buyFoodBtn = new Button("🛒 5x Futter kaufen (25 Gold)");
        buyFoodBtn.setOnAction(e -> {
            if (money >= 25) { money -= 25; foodStock += 5; updateUI(); } 
            else { showAlert("Zu wenig Geld!", "Du brauchst 25 Gold."); }
        });

        Button buyAlpacaBtn = new Button("🦙 Neues Alpaka kaufen (150 Gold)");
        buyAlpacaBtn.setOnAction(e -> {
            if (money >= 150) {
                money -= 150;
                Alpaca newAlpaca = new Alpaca();
                zooPets.add(newAlpaca);
                petSelector.getItems().add(newAlpaca.getName()); // Dropdown erweitern
                renderGehege();
                updateUI();
            } else { showAlert("Zu wenig Geld!", "Ein Alpaka kostet 150 Gold."); }
        });

        box.getChildren().addAll(new Label("Willkommen im Zoo-Shop!"), buyFoodBtn, buyAlpacaBtn);
        return box;
    }

    // --- HILFSMETHODEN ---
    private void updateUI() {
        moneyLabel.setText("💰 Geld: " + money + " Gold");
        foodLabel.setText("🌾 Futtervorrat: " + foodStock);
        
        if (currentPet != null) {
            careNameLabel.setText("Status von: " + currentPet.getName());
            hungerBar.setProgress(currentPet.getHunger() / 100.0);
            happinessBar.setProgress(currentPet.getHappiness() / 100.0);
            energyBar.setProgress(currentPet.getEnergy() / 100.0);
        }
    }

    // Lädt ein Bild pixelscharf aus dem Ressourcen-Ordner
    private ImageView loadSprite(String imageName) {
        try {
            // Holt das Bild aus src/resources/images/
            Image img = new Image(getClass().getResourceAsStream("/images/" + imageName + ".png"));
            ImageView view = new ImageView(img);
            view.setSmooth(false); // Verhindert Weichzeichner bei Pixelart!
            return view;
        } catch (Exception e) {
            // Fallback, falls ein Bild fehlt (zeigt ein leeres ImageView)
            System.out.println("Fehler: Konnte " + imageName + ".png nicht laden.");
            return new ImageView();
        }
    }

    private String getDirectionSuffix(int direction) {
        switch (direction) {
            case 0: return "_up";
            case 90: return "_right";
            case 180: return "_down";
            case 270: return "_left";
            default: return "_down";
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}