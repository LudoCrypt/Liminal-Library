package net.ludocrypt.limlib.api.world.maze;
/**
 * Dilates or scales a maze to be dilation times bigger.
 **/
public class DilateMaze extends MazeComponent {

	private MazeComponent mazeIn;
	private int dilationX;
	private int dilationY;

	public DilateMaze(MazeComponent mazeIn, int dilation) {
		this(mazeIn, dilation, dilation);
	}

	public DilateMaze(MazeComponent mazeIn, int dilationX, int dilationY) {
		super(mazeIn.width * dilationX, mazeIn.height * dilationY);
		this.mazeIn = mazeIn;
		this.dilationX = dilationX;
		this.dilationY = dilationY;
	}

	@Override
	public void create() {

		for (int x = 0; x < mazeIn.width; x++) {

			for (int y = 0; y < mazeIn.height; y++) {

				for (int dx = 0; dx < dilationX; dx++) {

					for (int dy = 0; dy < dilationY; dy++) {
						int mazeX = x * dilationX + dx;
						int mazeY = y * dilationY + dy;
						Vec2i position = new Vec2i(mazeX, mazeY);
						CellState reference = mazeIn.cellState(x, y);

						if (dx % dilationX == 0) {

							if (dy % dilationY == 0) {
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

							if (dy % dilationY == 0) {

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

	public int getDilationX() {
		return dilationX;
	}

	public int getDilationY() {
		return dilationY;
	}

}
