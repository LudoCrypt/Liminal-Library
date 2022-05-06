package net.ludocrypt.limlib.api.world;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import com.mojang.datafixers.util.Either;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public abstract class LiminalChunkGenerator extends ChunkGenerator {

	private final MultiNoiseSampler multiNoiseSampler;

	public LiminalChunkGenerator(Registry<StructureSet> registry, Optional<RegistryEntryList<StructureSet>> optional, BiomeSource biomeSource, BiomeSource biomeSource2, long l, MultiNoiseSampler multiNoiseSampler) {
		super(registry, optional, biomeSource, biomeSource2, l);
		this.multiNoiseSampler = multiNoiseSampler;
	}

	public abstract CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureManager structureManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> function, List<Chunk> chunks, Chunk chunk2, boolean bl);

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor var1, Blender var2, StructureAccessor var3, Chunk var4) {
		throw new UnsupportedOperationException("populateNoise should never be called in LiminalChunkGenerator");
	}

	public int chunkRadius() {
		return 1;
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
	public int getWorldHeight() {
		return 0;
	}

	@Override
	public int getHeight(int var1, int var2, Type var3, HeightLimitView var4) {
		return 0;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
		BlockState[] states = new BlockState[world.getHeight()];
		for (int i = 0; i < states.length; i++) {
			states[i] = Blocks.AIR.getDefaultState();
		}
		return new VerticalBlockSample(0, states);
	}

	@Override
	public void getDebugHudText(List<String> var1, BlockPos var2) {
	}

}
