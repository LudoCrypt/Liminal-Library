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
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

public abstract class LiminalChunkGenerator extends ChunkGenerator {

	public LiminalChunkGenerator(Registry<StructureSet> structureSetRegistry, Optional<RegistryEntryList<StructureSet>> structureOverrides, BiomeSource biomeSource) {
		super(structureSetRegistry, structureOverrides, biomeSource);
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noise, StructureAccessor structureAccessor, Chunk chunk) {
		throw new UnsupportedOperationException("populateNoise should never be called in LiminalChunkGenerator");
	}

	public abstract CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk, boolean regenerate);

	@Override
	public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noise, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, Carver carver) {
	}

	@Override
	public void buildSurface(ChunkRegion chunkRegion, StructureAccessor structureAccessor, NoiseConfig noise, Chunk chunk) {
	}

	@Override
	public void populateEntities(ChunkRegion chunkRegion) {
	}

	@Override
	public int getHeight(int var1, int var2, Type var3, HeightLimitView var4, NoiseConfig var5) {
		return this.getWorldHeight();
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int y, HeightLimitView world, NoiseConfig noise) {
		BlockState[] states = new BlockState[world.getHeight()];
		for (int i = 0; i < states.length; i++) {
			states[i] = Blocks.AIR.getDefaultState();
		}
		return new VerticalBlockSample(0, states);
	}

	@Override
	public void getDebugHudText(List<String> var1, NoiseConfig var2, BlockPos var3) {
	}

	public int chunkRadius() {
		return 1;
	}

}
