package net.ludocrypt.limlib.api.world;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
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
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;

public abstract class LiminalChunkGenerator extends ChunkGenerator {

	public final long worldSeed;
	public final MultiNoiseSampler multiNoiseSampler;

	public LiminalChunkGenerator(BiomeSource biomeSource, long worldSeed) {
		this(biomeSource, new FlatMultiNoiseSampler(0.0F), worldSeed);
	}

	public LiminalChunkGenerator(BiomeSource biomeSource, MultiNoiseSampler multiNoiseSampler, long worldSeed) {
		super(biomeSource, biomeSource, new StructuresConfig(Optional.empty(), new HashMap<StructureFeature<?>, StructureConfig>()), worldSeed);
		this.multiNoiseSampler = multiNoiseSampler;
		this.worldSeed = worldSeed;
	}

	@Override
	public MultiNoiseSampler getMultiNoiseSampler() {
		return this.multiNoiseSampler;
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
		throw new UnsupportedOperationException("populateNoise should never be called in " + this.getClass());
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
		BlockState[] states = new BlockState[world.getHeight()];
		for (int i = 0; i < states.length; i++) {
			states[i] = Blocks.AIR.getDefaultState();
		}
		return new VerticalBlockSample(0, states);
	}

	// impl

	public abstract CompletableFuture<Chunk> populateNoise(Executor executor, Chunk chunk, ChunkStatus targetStatus, ServerWorld world, ChunkRegion chunkRegion, StructureManager structureManager, ServerLightingProvider lightingProvider);

	public int getChunkRadius() {
		return 0;
	}

}
