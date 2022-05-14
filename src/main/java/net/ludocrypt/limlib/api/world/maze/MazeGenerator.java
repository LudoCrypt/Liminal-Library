package net.ludocrypt.limlib.api.world.maze;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.ludocrypt.limlib.api.world.maze.MazeComponent.CellState;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public abstract class MazeGenerator<T extends ChunkGenerator> {

	public static final Codec<MazeGenerator<? extends ChunkGenerator>> CODEC = LimlibRegistries.LIMINAL_MAZE_GENERATOR.getCodec().dispatchStable(MazeGenerator::getCodec, Function.identity());

	private final HashMap<BlockPos, MazeComponent> mazes = new HashMap<BlockPos, MazeComponent>(30);

	public final int width;
	public final int height;
	public final int thickness;
	public final boolean redundancy;
	public final long seedModifier;

	public MazeGenerator(int width, int height, int thickness, boolean redundancy, long seedModifier) {
		this.width = width;
		this.height = height;
		this.thickness = thickness;
		this.redundancy = redundancy;
		this.seedModifier = seedModifier;
	}

	public void generateMaze(BlockPos pos, Chunk chunk, ChunkRegion region, T chunkGenerator) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				BlockPos inPos = pos.add(x, 0, y);
				if (mod(inPos.getX(), thickness) == 0 && mod(inPos.getZ(), thickness) == 0) {
					BlockPos mazePos = new BlockPos(inPos.getX() - mod(inPos.getX(), (width * thickness)), 0, inPos.getZ() - mod(inPos.getZ(), (height * thickness)));

					MazeComponent maze;
					if (this.mazes.containsKey(mazePos)) {
						maze = this.mazes.get(mazePos);
					} else {
						maze = this.newMaze(mazePos, region, chunk, chunkGenerator, redundancy ? width + 4 : width, redundancy ? height + 4 : height, new Random(blockSeed(mazePos.getX(), mazePos.getZ(), seedModifier)));
						this.mazes.put(mazePos, maze);
					}

					int mazeX = (inPos.getX() - mazePos.getX()) / thickness;
					int mazeY = (inPos.getZ() - mazePos.getZ()) / thickness;

					CellState originCell = maze.cellState(redundancy ? mazeX + 2 : mazeX, redundancy ? mazeY + 2 : mazeY);

					this.decorateCell(inPos, mazePos, chunk, region, chunkGenerator, originCell, this.thickness);
				}
			}
		}
	}

	public abstract MazeComponent newMaze(BlockPos mazePos, ChunkRegion region, Chunk chunk, T chunkGenerator, int width, int height, Random random);

	public abstract void decorateCell(BlockPos pos, BlockPos mazePos, Chunk chunk, ChunkRegion region, T chunkGenerator, CellState state, int thickness);

	public HashMap<BlockPos, MazeComponent> getMazes() {
		return mazes;
	}

	public abstract Codec<? extends MazeGenerator<? extends ChunkGenerator>> getCodec();

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

}
