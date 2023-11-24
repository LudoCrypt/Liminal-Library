package net.ludocrypt.limlib.api.world.maze;

public class CombineMaze extends MazeComponent {

	MazeComponent[] components;

	public CombineMaze(MazeComponent... components) {
		super(components[0].width, components[0].height);
		this.components = components;
	}

	@Override
	public void create() {

		for (MazeComponent maze : components) {

			for (int x = 0; x < width; x++) {

				for (int y = 0; y < height; y++) {
					CellState reference = maze.cellState(x, y);

					for (Face face : Face.values()) {

						if (reference.goes(face)) {
							this.cellState(x, y).go(face);
						}

					}

					this.cellState(x, y).appendAll(reference.getExtra());
				}

			}

		}

	}

}
