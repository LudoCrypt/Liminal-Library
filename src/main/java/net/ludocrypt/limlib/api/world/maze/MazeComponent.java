package net.ludocrypt.limlib.api.world.maze;

import net.minecraft.util.math.BlockPos;

public abstract class MazeComponent {

	public final int width;
	public final int height;

	public final CellState[] maze;

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

	public abstract void generateMaze();

	public void generateDistances() {
		for (CellState cell : maze) {
			int shortestDistance = this.width * this.height;
			Iterable<BlockPos> iterable = BlockPos.iterateOutwards(new BlockPos(cell.getPosition().getX(), cell.getPosition().getY(), 0), this.width, this.height, 0);
			for (BlockPos pos : iterable) {
				if ((pos.getX() < this.width) && (pos.getY() < this.height) && (pos.getX() > 0) && (pos.getY() > 0)) {
					if (this.cellState(pos.getX(), pos.getY()).isNorth() || this.cellState(pos.getX(), pos.getY()).isEast() || this.cellState(pos.getX(), pos.getY()).isSouth() || this.cellState(pos.getX(), pos.getY()).isWest()) {
						int cellDistance = pos.getManhattanDistance(new BlockPos(cell.getPosition().getX(), cell.getPosition().getY(), 0));
						if (cellDistance < shortestDistance) {
							shortestDistance = cellDistance;
						}
					}
				}
				if (shortestDistance <= 1) {
					break;
				}
			}
			cell.setDistance(shortestDistance);
		}
	}

	public CellState cellState(int x, int y) {
		return this.maze[y * this.width + x];
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

	public static class CellState {

		private Vec2i position = new Vec2i(0, 0);
		private int distance = -1;
		private boolean north = false;
		private boolean east = false;
		private boolean south = false;
		private boolean west = false;
		private boolean visited = false;

		public CellState copy() {
			CellState newState = new CellState();
			newState.setPosition(this.position);
			newState.setDistance(this.distance);
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

		public void setDistance(int distance) {
			this.distance = distance;
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

		public int getDistance() {
			return distance;
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
