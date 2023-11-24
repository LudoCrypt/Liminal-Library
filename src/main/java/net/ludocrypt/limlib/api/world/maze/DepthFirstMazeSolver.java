package net.ludocrypt.limlib.api.world.maze;

import java.util.List;
import java.util.Stack;

import com.google.common.collect.Lists;

import net.minecraft.util.random.RandomGenerator;

/**
 * Solves a maze using the Depth First Search algorithm.
 **/
public class DepthFirstMazeSolver extends DepthLikeMaze {

	private final MazeComponent mazeToSolve;
	private final Vec2i end;
	private final List<Vec2i> beginnings;
	public final RandomGenerator random;

	/**
	 * Creates a depth first maze solver.
	 * <p>
	 * 
	 * @param mazeToSolve is the maze to solve
	 * @param random      is the random
	 * @param end         is the position for the depth first algorithm to find
	 * @param beginnings  are the positions to start from
	 **/
	public DepthFirstMazeSolver(MazeComponent mazeToSolve, RandomGenerator random, Vec2i end, Vec2i... beginnings) {
		super(mazeToSolve.width, mazeToSolve.height);
		this.mazeToSolve = mazeToSolve;
		this.end = end;
		this.beginnings = Lists.newArrayList(beginnings);
		this.random = random;
	}

	@Override
	public void create() {
		List<Stack<Vec2i>> paths = Lists.newArrayList();
		this.beginnings.forEach((beginning) -> {
			Stack<Vec2i> stack = new Stack<Vec2i>();
			stack.push(new Vec2i(beginning.getX(), beginning.getY()));
			Vec2i peek = stack.peek();
			visit(peek);

			while (!peek.equals(end)) {
				List<Face> neighbours = Lists.newArrayList();

				for (Face face : Face.values()) {

					if (this.hasNeighbour(peek, face)) {
						neighbours.add(face);
					}

				}

				if (!neighbours.isEmpty()) {
					Face nextFace = neighbours.get(random.nextInt(neighbours.size()));

					visit(peek.go(nextFace));
					stack.push(peek.go(nextFace));

				} else {
					stack.pop();
				}

				peek = stack.peek();
			}

			for (int x = 0; x < width; x++) {

				for (int y = 0; y < height; y++) {
					visit(new Vec2i(x, y), false);
				}

			}

			paths.add(stack);
		});
		paths.forEach((path) -> {

			for (int i = 0; i < path.size(); i++) {
				Vec2i pos = path.get(i);

				if (i + 1 != path.size()) {
					Vec2i nextPos = path.get(i + 1);

					Face face = pos.normal(nextPos);
					this.cellState(pos).go(face);
					this.cellState(pos.go(face)).go(face.mirror());

					this.cellState(pos).appendAll(mazeToSolve.cellState(pos).getExtra());

				}

				if (this.beginnings.contains(pos) || pos.equals(this.end)) {

					if (pos.getX() == 0) {
						this.cellState(pos).down();
					}

					if (pos.getY() == 0) {
						this.cellState(pos).left();
					}

					if (pos.getX() == width - 1) {
						this.cellState(pos).up();
					}

					if (pos.getY() == height - 1) {
						this.cellState(pos).right();
					}

				}

			}

		});
	}

	public MazeComponent getMazeToSolve() {
		return mazeToSolve;
	}

	@Override
	public boolean hasNeighbourUp(Vec2i vec) {
		return super.hasNeighbourUp(vec) && this.mazeToSolve.cellState(vec).goesUp();
	}

	@Override
	public boolean hasNeighbourRight(Vec2i vec) {
		return super.hasNeighbourRight(vec) && this.mazeToSolve.cellState(vec).goesRight();
	}

	@Override
	public boolean hasNeighbourDown(Vec2i vec) {
		return super.hasNeighbourDown(vec) && this.mazeToSolve.cellState(vec).goesDown();
	}

	@Override
	public boolean hasNeighbourLeft(Vec2i vec) {
		return super.hasNeighbourLeft(vec) && this.mazeToSolve.cellState(vec).goesLeft();
	}

}
