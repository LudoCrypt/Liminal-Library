package net.ludocrypt.limlib.api.world.maze;
/**
 * Dilates or scales a maze to be dilation times bigger.
 **/
public class DilateMaze extends MazeComponent {

	private MazeComponent mazeIn;
	private int dilation;

	public DilateMaze(MazeComponent mazeIn, int dilation) {
		super(mazeIn.width * dilation, mazeIn.height * dilation);
		this.mazeIn = mazeIn;
		this.dilation = dilation;
	}

	@Override
	public void generateMaze() {

		for (int x = 0; x < mazeIn.width; x++) {

			for (int y = 0; y < mazeIn.height; y++) {

				for (int dx = 0; dx < dilation; dx++) {

					for (int dy = 0; dy < dilation; dy++) {
						int mazeX = x * dilation + dx;
						int mazeY = y * dilation + dy;
						Vec2i position = new Vec2i(mazeX, mazeY);
						CellState reference = mazeIn.cellState(x, y);

						if (dx % dilation == 0) {

							if (dy % dilation == 0) {
								CellState copy = reference.copy();
								copy.setPosition(position);
								this.maze[mazeY * this.width + mazeX] = copy;
								this.solvedMaze.add(position);
							} else {

								if (mazeIn.cellState(x, y).isEast()) {
									CellState copy = new CellState();
									copy.east();
									copy.west();
									copy.setPosition(position);
									copy.appendAll(reference.getExtra());
									this.maze[mazeY * this.width + mazeX] = copy;
									this.solvedMaze.add(position);
								}

							}

						} else {

							if (dy % dilation == 0) {

								if (mazeIn.cellState(x, y).isNorth()) {
									CellState copy = new CellState();
									copy.north();
									copy.south();
									copy.setPosition(position);
									copy.appendAll(reference.getExtra());
									this.maze[mazeY * this.width + mazeX] = copy;
									this.solvedMaze.add(position);
								}

							} else {
								CellState copy = new CellState();
								copy.setPosition(position);
								copy.appendAll(reference.getExtra());
								this.maze[mazeY * this.width + mazeX] = copy;
							}

						}

					}

				}

			}

		}

	}

	public MazeComponent getMazeIn() {
		return mazeIn;
	}

	public int getDilation() {
		return dilation;
	}

}
