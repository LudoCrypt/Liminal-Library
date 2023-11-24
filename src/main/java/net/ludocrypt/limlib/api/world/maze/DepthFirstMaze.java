package net.ludocrypt.limlib.api.world.maze;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.random.RandomGenerator;

public class DepthFirstMaze extends DepthLikeMaze {

	public RandomGenerator random;

	public DepthFirstMaze(int width, int height, RandomGenerator RandomGenerator) {
		super(width, height);
		this.random = RandomGenerator;
	}

	@Override
	public void create() {
		visit(new Vec2i(0, 0));
		this.visitedCells++;
		this.stack.push(new Vec2i(0, 0));

		while (visitedCells < this.width * this.height) {
			List<Face> neighbours = Lists.newArrayList();

			for (Face face : Face.values()) {

				if (this.hasNeighbour(this.stack.peek(), face)) {
					neighbours.add(face);
				}

			}

			if (!neighbours.isEmpty()) {
				Face nextFace = neighbours.get(random.nextInt(neighbours.size()));

				this.cellState(this.stack.peek()).go(nextFace);
				this.cellState(this.stack.peek().go(nextFace)).go(nextFace.mirror());
				this.visit(this.stack.peek().go(nextFace));
				this.stack.push(this.stack.peek().go(nextFace));

				this.visitedCells++;

			} else {
				this.stack.pop();
			}

		}

	}

}
