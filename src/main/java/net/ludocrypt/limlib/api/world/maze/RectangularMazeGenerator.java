package net.ludocrypt.limlib.api.world.maze;

import java.util.HashMap;

import net.ludocrypt.limlib.api.world.maze.MazeComponent.CellState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;

/**
 * A rectangular maze generator class
 */
public abstract class RectangularMazeGenerator<M extends MazeComponent> {

	private final HashMap<BlockPos, M> mazes = new HashMap<BlockPos, M>(30);

	public final int width;
	public final int height;
	public final int thickness;
	public final boolean redundancy;
	public final long seedModifier;

	/**
	 * Creates a rectangular maze generator.
	 * <p>
	 * 
	 * @param width        of the maze
	 * @param height       of the maze
	 * @param thickness    of the walls. This helps to know where to place adjacent
	 *                     cells.
	 * @param redundancy   makes a maze 2 blocks outwards in each direction, and
	 *                     only generates the centre part of the maze. This helps
	 *                     create the illusion of a larger structure at work.
	 * @param seedModifier is the change to the seed when generating a new random
	 *                     maze. In code, it uses the world seed + seedModifier
	 */
	public RectangularMazeGenerator(int width, int height, int thickness, boolean redundancy, long seedModifier) {
		this.width = width;
		this.height = height;
		this.thickness = thickness;
		this.redundancy = redundancy;
		this.seedModifier = seedModifier;
	}

	/**
	 * Begins generating a maze starting at pos. This should be run in every chunk
	 * with the pos being the beginning position of the chunk.
	 * 
	 * @param pos           the starting position for the maze logic to work.
	 * @param seed          the world seed
	 * @param mazeCreator   functional interface to create a new maze at a position
	 * @param cellDecorator funcional interface to generate a single maze block, or
	 *                      'cell'
	 */
	public void generateMaze(BlockPos pos, long seed, MazeCreator<M> mazeCreator, CellDecorator<M> cellDecorator) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				BlockPos inPos = pos.add(x, 0, y);
				if (mod(inPos.getX(), thickness) == 0 && mod(inPos.getZ(), thickness) == 0) {
					BlockPos mazePos = new BlockPos(inPos.getX() - mod(inPos.getX(), (width * thickness)), 0, inPos.getZ() - mod(inPos.getZ(), (height * thickness)));

					M maze;
					if (this.mazes.containsKey(mazePos)) {
						maze = this.mazes.get(mazePos);
					} else {
						maze = mazeCreator.newMaze(mazePos, redundancy ? width + 4 : width, redundancy ? height + 4 : height,
								RandomGenerator.createLegacy(blockSeed(mazePos.getX(), mazePos.getZ(), seed + seedModifier)));
						this.mazes.put(mazePos, maze);
					}

					int mazeX = (inPos.getX() - mazePos.getX()) / thickness;
					int mazeY = (inPos.getZ() - mazePos.getZ()) / thickness;

					CellState originCell = maze.cellState(redundancy ? mazeX + 2 : mazeX, redundancy ? mazeY + 2 : mazeY);

					cellDecorator.generate(inPos, mazePos, maze, originCell, this.thickness);
				}
			}
		}
	}

	public HashMap<BlockPos, M> getMazes() {
		return mazes;
	}

	protected int mod(int x, int n) {
		int r = x % n;
		if (r < 0) {
			r += n;
		}
		return r;
	}

	protected long blockSeed(long x, long y, long z) {
		long l = (x * 3129871) ^ z * 116129781L ^ y;
		l = l * l * 42317861L + l * 11L;
		return l >> 16;
	}

	@FunctionalInterface
	public static interface CellDecorator<M extends MazeComponent> {
		void generate(BlockPos pos, BlockPos mazePos, M maze, CellState state, int thickness);
	}

	@FunctionalInterface
	public static interface MazeCreator<M extends MazeComponent> {
		M newMaze(BlockPos mazePos, int width, int height, RandomGenerator random);
	}

}
