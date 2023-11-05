package net.ludocrypt.limlib.api.world.maze;

import java.util.List;
import java.util.Stack;

import com.google.common.collect.Lists;

import net.minecraft.util.random.RandomGenerator;

/**
 * Solves a maze using the Depth First Search algorithm.
 **/
public class DepthFirstMazeSolver extends MazeComponent {

	private final MazeComponent mazeToSolve;
	private final Vec2i start;
	private final List<Vec2i> ends;
	public final RandomGenerator random;

	/**
	 * Creates a depth first maze solver.
	 * <p>
	 * 
	 * @param mazeToSolve is the maze to solve
	 * @param start       is the position for the depth first algorithm to start at
	 * @param ends        are the positions to look for before being 'solved'
	 * @param random      is the random
	 **/
	public DepthFirstMazeSolver(MazeComponent mazeToSolve, Vec2i start, List<Vec2i> ends, RandomGenerator random) {
		super(mazeToSolve.width, mazeToSolve.height);
		this.mazeToSolve = mazeToSolve;
		this.start = start;
		this.ends = ends;
		this.random = random;
	}

	@Override
	public void generateMaze() {
		List<Stack<Vec2i>> paths = Lists.newArrayList();
		this.ends.forEach((end) -> {
			Stack<Vec2i> stack = new Stack<Vec2i>();
			stack.push(new Vec2i(start.getX(), start.getY()));
			Vec2i peek = stack.peek();

			while (!peek.equals(end)) {
				List<Integer> neighbours = Lists.newArrayList();

				// North Neighbour
				if (this.hasNorthNeighbor(stack.peek())) {
					neighbours.add(0);
				}

				// East Neighbour
				if (this.hasEastNeighbor(stack.peek())) {
					neighbours.add(1);
				}

				// South Neighbour
				if (this.hasSouthNeighbor(stack.peek())) {
					neighbours.add(2);
				}

				// West Neighbour
				if (this.hasWestNeighbor(stack.peek())) {
					neighbours.add(3);
				}

				if (!neighbours.isEmpty()) {
					int nextCellDir = neighbours.get(random.nextInt(neighbours.size()));

					switch (nextCellDir) {
						case 0: // North
							this.cellState(stack.peek().getX() + 1, stack.peek().getY()).visited();
							stack.push(new Vec2i(stack.peek().getX() + 1, stack.peek().getY()));
							break;
						case 1: // East
							this.cellState(stack.peek().getX(), stack.peek().getY() + 1).visited();
							stack.push(new Vec2i(stack.peek().getX(), stack.peek().getY() + 1));
							break;
						case 2: // South
							this.cellState(stack.peek().getX() - 1, stack.peek().getY()).visited();
							stack.push(new Vec2i(stack.peek().getX() - 1, stack.peek().getY()));
							break;
						case 3: // West
							this.cellState(stack.peek().getX(), stack.peek().getY() - 1).visited();
							stack.push(new Vec2i(stack.peek().getX(), stack.peek().getY() - 1));
							break;
					}

				} else {
					stack.pop();
				}

				peek = stack.peek();
			}

			for (int x = 0; x < width; x++) {

				for (int y = 0; y < height; y++) {
					this.maze[y * this.width + x].setVisited(false);
				}

			}

			paths.add(stack);
		});
		paths.forEach((path) -> {

			for (int i = 0; i < path.size(); i++) {
				Vec2i pos = path.get(i);

				if (i + 1 != path.size()) {
					Vec2i nextPos = path.get(i + 1);

					if (nextPos.equals(new Vec2i(pos.getX() + 1, pos.getY()))) { // North
						this.cellState(pos).north();
						this.cellState(nextPos).south();
					} else if (nextPos.equals(new Vec2i(pos.getX(), pos.getY() + 1))) { // East
						this.cellState(pos).east();
						this.cellState(nextPos).west();
					} else if (nextPos.equals(new Vec2i(pos.getX() - 1, pos.getY()))) { // South
						this.cellState(pos).south();
						this.cellState(nextPos).north();
					} else if (nextPos.equals(new Vec2i(pos.getX(), pos.getY() - 1))) { // West
						this.cellState(pos).west();
						this.cellState(nextPos).east();
					}

					this.cellState(pos).appendAll(mazeToSolve.cellState(pos).getExtra());

					if (!this.solvedMaze.contains(new Vec2i(pos.getX(), pos.getY()))) {
						this.solvedMaze.add(new Vec2i(pos.getX(), pos.getY()));
					}

				}

				if (this.ends.contains(pos) || pos.equals(this.start)) {

					if (pos.getX() == 0) {
						this.cellState(pos).south();
					}

					if (pos.getY() == 0) {
						this.cellState(pos).west();
					}

					if (pos.getX() == width - 1) {
						this.cellState(pos).north();
					}

					if (pos.getY() == height - 1) {
						this.cellState(pos).east();
					}

				}

			}

		});
	}

	public MazeComponent getMazeToSolve() {
		return mazeToSolve;
	}

	@Override
	public boolean hasNorthNeighbor(Vec2i vec) {
		return super.hasNorthNeighbor(vec) && this.mazeToSolve.cellState(vec.getX(), vec.getY()).isNorth();
	}

	@Override
	public boolean hasEastNeighbor(Vec2i vec) {
		return super.hasEastNeighbor(vec) && this.mazeToSolve.cellState(vec.getX(), vec.getY()).isEast();
	}

	@Override
	public boolean hasSouthNeighbor(Vec2i vec) {
		return super.hasSouthNeighbor(vec) && this.mazeToSolve.cellState(vec.getX(), vec.getY()).isSouth();
	}

	@Override
	public boolean hasWestNeighbor(Vec2i vec) {
		return super.hasWestNeighbor(vec) && this.mazeToSolve.cellState(vec.getX(), vec.getY()).isWest();
	}

}
