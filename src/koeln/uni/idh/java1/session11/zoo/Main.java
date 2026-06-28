package koeln.uni.idh.java1.session11.zoo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Main().createAndShowGui());
	}

	private void createAndShowGui() {
		JFrame frame = new JFrame("Mein interaktiver Zoo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(10, 10));

		List<WalkingMammal> animals = Arrays.asList(createAlpaca(), createElephant(), createHorse());

		JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		buttonPanel.setPreferredSize(new Dimension(240, 180));

		for (WalkingMammal animal : animals) {
			JButton button = new JButton("Tier öffnen: " + animal.getDisplayName());
			button.addActionListener(e -> new AnimalWindow(animal).show());
			buttonPanel.add(button);
		}

		JLabel infoLabel = new JLabel("Klicke ein Tier, um seinen Zustand und ASCII-Art zu sehen.");
		frame.add(infoLabel, BorderLayout.NORTH);
		frame.add(buttonPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private Alpaca createAlpaca() {
		Alpaca alpaca = new Alpaca();
		alpaca.setName("Alpaka");
		return alpaca;
	}

	private Elephant createElephant() {
		Elephant elephant = new Elephant();
		elephant.setName("Elefant");
		return elephant;
	}

	private Horse createHorse() {
		Horse horse = new Horse("Hugo", "braun");
		horse.setName("Pferd Hugo");
		return horse;
	}

	private static class AnimalWindow {
		private final WalkingMammal animal;
		private final JFrame frame;
		private final JTextArea artArea;
		private final JLabel statusLabel;

		AnimalWindow(WalkingMammal animal) {
			this.animal = animal;
			this.frame = new JFrame(animal.getDisplayName());
			this.artArea = new JTextArea();
			this.statusLabel = new JLabel();
			setupWindow();
		}

		private void setupWindow() {
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLayout(new BorderLayout(8, 8));

			artArea.setEditable(false);
			artArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
			artArea.setText(animal.getAsciiArt());
			frame.add(new JScrollPane(artArea), BorderLayout.CENTER);

			statusLabel.setText(animal.getNeedStatus());
			frame.add(statusLabel, BorderLayout.NORTH);

			JPanel controlPanel = new JPanel(new GridLayout(1, 3, 5, 5));
			controlPanel.add(createActionButton("Füttern", () -> animal.feed(), "Hunger wird reduziert."));
			controlPanel.add(createActionButton("Trinken", () -> animal.drink(), "Durst wird reduziert."));
			controlPanel.add(createActionButton("Ausruhen", () -> animal.rest(), "Müdigkeit wird reduziert."));
			frame.add(controlPanel, BorderLayout.SOUTH);

			frame.pack();
			frame.setLocationRelativeTo(null);
		}

		private JButton createActionButton(String title, Runnable action, String feedback) {
			JButton button = new JButton(title);
			button.addActionListener(e -> {
				action.run();
				updateDisplay();
				JOptionPane.showMessageDialog(frame, feedback, title, JOptionPane.INFORMATION_MESSAGE);
			});
			return button;
		}

		private void updateDisplay() {
			artArea.setText(animal.getAsciiArt());
			statusLabel.setText(animal.getNeedStatus());
		}

		void show() {
			frame.setVisible(true);
		}
	}
}
