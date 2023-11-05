package net.ludocrypt.limlib.api.world.maze;
public class CombineMaze extends MazeComponent {

	MazeComponent[] components;

	public CombineMaze(MazeComponent... components) {
		super(components[0].width, components[0].height);
		this.components = components;
	}

	@Override
	public void generateMaze() {

		for (MazeComponent maze : components) {

			for (Vec2i pos : maze.solvedMaze) {
				int x = pos.getX();
				int y = pos.getY();
				CellState reference = maze.cellState(x, y);

				if (reference.isNorth()) {
					this.cellState(x, y).setNorth(true);
				}

				if (reference.isEast()) {
					this.cellState(x, y).setEast(true);
				}

				if (reference.isSouth()) {
					this.cellState(x, y).setSouth(true);
				}

				if (reference.isWest()) {
					this.cellState(x, y).setWest(true);
				}

				if (this.cellState(x, y).isNorth() || this.cellState(x, y).isEast() || this.cellState(x, y).isSouth() || this.cellState(x, y).isWest()) {
					this.solvedMaze.add(new Vec2i(x, y));
				}

				this.cellState(x, y).appendAll(reference.getExtra());
			}

		}

	}

}
