package net.ludocrypt.limlib.api.world;

import java.util.function.BiFunction;

import org.apache.commons.lang3.function.TriFunction;

import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.impl.LiminalEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class LiminalWorld {

	public final String world;
	public final Identifier worldId;
	public final DimensionType worldDimensionType;
	public final TriFunction<Registry<DimensionType>, Registry<Biome>, Long, DimensionOptions> worldDimensionOptions;
	public final RegistryKey<DimensionType> worldDimensionTypeRegistryKey;
	public final RegistryKey<DimensionOptions> worldDimensionOptionsRegistryKey;
	public final RegistryKey<World> worldWorldRegistryKey;

	public LiminalWorld(Identifier id, DimensionType dimensionType, BiFunction<Registry<Biome>, Long, ChunkGenerator> chunkGenerator, LiminalEffects liminalEffects) {
		this.world = id.getPath();
		this.worldId = id;
		this.worldDimensionTypeRegistryKey = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, worldId);
		this.worldDimensionOptionsRegistryKey = RegistryKey.of(Registry.DIMENSION_KEY, worldId);
		this.worldWorldRegistryKey = RegistryKey.of(Registry.WORLD_KEY, worldId);
		this.worldDimensionType = dimensionType;
		this.worldDimensionOptions = (dimensionRegistry, biomeRegistry, seed) -> new DimensionOptions(() -> dimensionRegistry.getOrThrow(this.worldDimensionTypeRegistryKey), chunkGenerator.apply(biomeRegistry, seed));
		((DimensionTypeAccess) this.worldDimensionType).setLiminalEffects(liminalEffects);
	}

}
