package koeln.uni.idh.java1.session11.zoo.areas;

import java.util.ArrayList;
import java.util.List;

public class ZooMap {
	private final int width;
	private final int height;
	private final List<Habitat> habitats;

	public ZooMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.habitats = new ArrayList<>();
	}

	public void addHabitat(Habitat habitat, int x, int y) {
		habitats.add(habitat);
		habitat.setPosition(x, y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<Habitat> getHabitats() {
		return habitats;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Zoo map ").append(width).append("x").append(height).append("\n");
		for (Habitat habitat : habitats) {
			builder.append("- ").append(habitat.getName()).append(" at (")
				.append(habitat.getX()).append(", ").append(habitat.getY())
				.append(")\n");
		}
		return builder.toString();
	}
}
