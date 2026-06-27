package koeln.uni.idh.java1.session11.zoo.engine;

import java.util.List;

/**
 * Ein einfacher Dialog-/Cutscene-Zustand: zeigt nacheinander mehrere Textseiten
 * (jeweils Sprecher + Text). Mit jedem Tastendruck blättert der Spieler eine
 * Seite weiter; nach der letzten Seite wird {@link #onFinish} ausgeführt (das
 * z. B. in den nächsten Zustand wechselt).
 *
 * Wiederverwendbar für das Intro, des Professors Kampfansage und den Abspann.
 */
public class DialogueState implements GameState {

	/** Eine Dialogseite: wer spricht und was. Sprecher darf null sein (Erzähler). */
	public static class Page {
		private final String speaker;
		private final String text;

		public Page(String speaker, String text) {
			this.speaker = speaker;
			this.text = text;
		}

		public String getSpeaker() {
			return speaker;
		}

		public String getText() {
			return text;
		}
	}

	private final Game game;
	private final List<Page> pages;
	private final Runnable onFinish;
	private int index = 0;

	public DialogueState(Game game, List<Page> pages, Runnable onFinish) {
		this.game = game;
		this.pages = pages;
		this.onFinish = onFinish;
	}

	@Override
	public void render() {
		Page page = pages.get(index);
		boolean hasMore = index < pages.size() - 1;
		game.getRenderer().renderDialogue(page.getSpeaker(), page.getText(), hasMore);
	}

	@Override
	public void handleInput(char key) {
		// Jede Taste blättert weiter.
		index++;
		if (index >= pages.size()) {
			onFinish.run();
		}
	}
}
