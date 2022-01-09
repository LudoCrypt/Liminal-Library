package net.ludocrypt.limlib.api.world;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.ludocrypt.limlib.mixin.BlockEntityAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public abstract class NbtChunkGenerator extends ChunkGenerator {

	public final HashMap<String, NbtPlacerUtil> structures = new HashMap<String, NbtPlacerUtil>(30);

	public final long worldSeed;
	public final Identifier nbtId;
	public final MultiNoiseSampler multiNoiseSampler;

	public NbtChunkGenerator(BiomeSource biomeSource, MultiNoiseSampler multiNoiseSampler, long worldSeed, Identifier nbtId) {
		super(biomeSource, biomeSource, new StructuresConfig(false), worldSeed);
		this.multiNoiseSampler = multiNoiseSampler;
		this.worldSeed = worldSeed;
		this.nbtId = nbtId;
	}

	@Override
	public MultiNoiseSampler getMultiNoiseSampler() {
		return multiNoiseSampler;
	}

	@Override
	public void carve(ChunkRegion var1, long var2, BiomeAccess var4, StructureAccessor var5, Chunk var6, Carver var7) {
	}

	@Override
	public void buildSurface(ChunkRegion var1, StructureAccessor var2, Chunk var3) {
	}

	@Override
	public void populateEntities(ChunkRegion var1) {

	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor var1, Blender var2, StructureAccessor var3, Chunk var4) {
		throw new UnsupportedOperationException("populateNoise should never be called in instanceof ChunkGeneratorExtraInfo");
	}

	public abstract CompletableFuture<Chunk> populateNoise(Executor executor, Chunk chunk, ChunkStatus targetStatus, ServerWorld world, ChunkRegion chunkRegion, StructureManager structureManager, ServerLightingProvider lightingProvider);

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinimumY() {
		return 0;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
		return new VerticalBlockSample(0, new BlockState[world.getHeight()]);
	}

	protected void store(String id, ServerWorld world) {
		structures.put(id, NbtPlacerUtil.load(world.getServer().getResourceManager(), new Identifier(this.nbtId.getNamespace(), "nbt/" + this.nbtId.getPath() + "/" + id + ".nbt")).get());
	}

	protected void store(String id, ServerWorld world, int from, int to) {
		for (int i = from; i <= to; i++) {
			store(id + "_" + i, world);
		}
	}

	protected void generateNbt(ChunkRegion region, BlockPos at, String id) {
		generateNbt(region, at, id, BlockRotation.NONE);
	}

	protected void generateNbt(ChunkRegion region, BlockPos at, String id, BlockRotation rotation) {
		structures.get(id).rotate(rotation).generateNbt(region, at, (pos, state, nbt) -> {
			if (!state.isAir()) {
				if (state.isOf(Blocks.BARREL)) {
					region.setBlockState(pos, state, Block.NOTIFY_ALL, 1);
					if (region.getBlockEntity(pos)instanceof BarrelBlockEntity barrel) {
						barrel.setLootTable(LootTables.SIMPLE_DUNGEON_CHEST, region.getSeed() + MathHelper.hashCode(pos));
					}
				} else if (state.isOf(Blocks.BARRIER)) {
					region.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL, 1);
				} else {
					region.setBlockState(pos, state, Block.NOTIFY_ALL, 1);
				}
				BlockEntity blockEntity = region.getBlockEntity(pos);
				if (blockEntity != null) {
					if (state.isOf(blockEntity.getCachedState().getBlock())) {
						((BlockEntityAccessor) blockEntity).callWriteNbt(nbt);
					}
				}
			}
		}).spawnEntities(region, at, rotation);
	}

}
