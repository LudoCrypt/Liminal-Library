package net.ludocrypt.limlib.api.world.maze;

import java.util.List;
import java.util.Stack;

import com.google.common.collect.Lists;

import net.minecraft.util.math.random.Random;

public class DepthFirstMaze extends MazeComponent {

	public Stack<Vec2i> stack = new Stack<Vec2i>();

	public Random random;

	public DepthFirstMaze(int width, int height, Random random) {
		super(width, height);
		this.random = random;
	}

	@Override
	public void generateMaze() {
		this.maze[0].visited();
		this.stack.push(new Vec2i(0, 0));
		while (visitedCells < this.width * this.height) {
			List<Integer> neighbours = Lists.newArrayList();

			// North Neighbour
			if (this.hasNorthNeighbor(this.stack.peek())) {
				neighbours.add(0);
			}

			// East Neighbour
			if (this.hasEastNeighbor(this.stack.peek())) {
				neighbours.add(1);
			}

			// South Neighbour
			if (this.hasSouthNeighbor(this.stack.peek())) {
				neighbours.add(2);
			}

			// West Neighbour
			if (this.hasWestNeighbor(this.stack.peek())) {
				neighbours.add(3);
			}

			// Neighbour check
			if (!neighbours.isEmpty()) {
				int nextCellDir = neighbours.get(random.nextInt(neighbours.size()));

				switch (nextCellDir) {
				case 0: // North
					this.cellState(this.stack.peek().getX(), this.stack.peek().getY()).north();
					this.cellState(this.stack.peek().getX() + 1, this.stack.peek().getY()).south();
					this.cellState(this.stack.peek().getX() + 1, this.stack.peek().getY()).visited();
					this.stack.push(new Vec2i(this.stack.peek().getX() + 1, this.stack.peek().getY()));
					break;
				case 1: // East
					this.cellState(this.stack.peek().getX(), this.stack.peek().getY()).east();
					this.cellState(this.stack.peek().getX(), this.stack.peek().getY() + 1).west();
					this.cellState(this.stack.peek().getX(), this.stack.peek().getY() + 1).visited();
					this.stack.push(new Vec2i(this.stack.peek().getX(), this.stack.peek().getY() + 1));
					break;
				case 2: // South
					this.cellState(this.stack.peek().getX(), this.stack.peek().getY()).south();
					this.cellState(this.stack.peek().getX() - 1, this.stack.peek().getY()).north();
					this.cellState(this.stack.peek().getX() - 1, this.stack.peek().getY()).visited();
					this.stack.push(new Vec2i(this.stack.peek().getX() - 1, this.stack.peek().getY()));
					break;
				case 3: // West
					this.cellState(this.stack.peek().getX(), this.stack.peek().getY()).west();
					this.cellState(this.stack.peek().getX(), this.stack.peek().getY() - 1).east();
					this.cellState(this.stack.peek().getX(), this.stack.peek().getY() - 1).visited();
					this.stack.push(new Vec2i(this.stack.peek().getX(), this.stack.peek().getY() - 1));
					break;
				}

				if (!this.solvedMaze.contains(new Vec2i(this.stack.peek().getX(), this.stack.peek().getY()))) {
					this.solvedMaze.add(new Vec2i(this.stack.peek().getX(), this.stack.peek().getY()));
				}

				// Visit Cell
				this.visitedCells++;

			} else {
				// Backtrack
				this.stack.pop();
			}
		}
	}

}
