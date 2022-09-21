package net.ludocrypt.limlib.world.maze;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.math.BlockPos;

/**
 * Abstract maze component
 **/
public abstract class MazeComponent {

	public final int width;
	public final int height;

	public final CellState[] maze;
	public final List<Vec2i> solvedMaze = Lists.newArrayList();

	public int visitedCells = 0;

	public MazeComponent(int width, int height) {
		this.width = width;
		this.height = height;
		this.maze = new CellState[width * height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				CellState state = new CellState();
				state.setPosition(new Vec2i(x, y));
				this.maze[y * this.width + x] = state;
			}
		}
		this.visitedCells = 1;
	}

	/**
	 * Generates the full maze
	 **/
	public abstract void generateMaze();

	public CellState cellState(int x, int y) {
		return this.maze[y * this.width + x];
	}

	public CellState cellState(Vec2i pos) {
		return this.cellState(pos.getX(), pos.getY());
	}

	public boolean hasNorthNeighbor(Vec2i vec) {
		return (vec.getX() + 1 < this.height) && !(this.maze[((vec.getY()) * this.width + (vec.getX() + 1))].isVisited());
	}

	public boolean hasEastNeighbor(Vec2i vec) {
		return (vec.getY() + 1 < this.width) && !(this.maze[((vec.getY() + 1) * this.width + (vec.getX()))].isVisited());
	}

	public boolean hasSouthNeighbor(Vec2i vec) {
		return (vec.getX() > 0) && !(this.maze[((vec.getY()) * this.width + (vec.getX() - 1))].isVisited());
	}

	public boolean hasWestNeighbor(Vec2i vec) {
		return (vec.getY() > 0) && !(this.maze[((vec.getY() - 1) * this.width + (vec.getX()))].isVisited());
	}

	public boolean hasNeighbors(Vec2i vec) {
		return this.hasNorthNeighbor(vec) || this.hasEastNeighbor(vec) || this.hasSouthNeighbor(vec) || this.hasWestNeighbor(vec);
	}

	/**
	 * Describes the state of a particular room or 'cell' in a maze
	 * <p>
	 * 
	 * @param north   has north wall open
	 * @param east    has east wall open
	 * @param south   has south wall open
	 * @param west    has west wall open
	 * @param visited was already visited by the maze algorithm
	 **/
	public static class CellState {

		private Vec2i position = new Vec2i(0, 0);
		private boolean north = false;
		private boolean east = false;
		private boolean south = false;
		private boolean west = false;
		private boolean visited = false;

		public CellState copy() {
			CellState newState = new CellState();
			newState.setPosition(this.position);
			newState.setNorth(this.north);
			newState.setEast(this.east);
			newState.setSouth(this.south);
			newState.setWest(this.west);
			return newState;
		}

		public void north() {
			this.north = true;
		}

		public void east() {
			this.east = true;
		}

		public void south() {
			this.south = true;
		}

		public void west() {
			this.west = true;
		}

		public void visited() {
			this.visited = true;
		}

		public void setPosition(Vec2i position) {
			this.position = position;
		}

		public void setNorth(boolean north) {
			this.north = north;
		}

		public void setEast(boolean east) {
			this.east = east;
		}

		public void setSouth(boolean south) {
			this.south = south;
		}

		public void setWest(boolean west) {
			this.west = west;
		}

		public void setVisited(boolean visited) {
			this.visited = visited;
		}

		public Vec2i getPosition() {
			return position;
		}

		public boolean isNorth() {
			return north;
		}

		public boolean isEast() {
			return east;
		}

		public boolean isSouth() {
			return south;
		}

		public boolean isWest() {
			return west;
		}

		public boolean isVisited() {
			return visited;
		}

	}

	public static class Vec2i {

		private int x;
		private int y;

		public Vec2i(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public BlockPos toPos() {
			return new BlockPos(x, y, 0);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Vec2i pos) {
				return pos.x == this.x && pos.y == this.y;
			}
			return super.equals(obj);
		}

		@Override
		public String toString() {
			return "(" + this.x + ", " + this.y + ")";
		}

	}

}
