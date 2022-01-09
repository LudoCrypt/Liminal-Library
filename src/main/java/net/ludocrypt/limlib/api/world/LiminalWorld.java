package net.ludocrypt.limlib.api.world;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class LiminalWorld {

	public final String world;
	public final Identifier worldId;
	public final DimensionType worldDimensionType;
	public final BiFunction<Registry<DimensionType>, Long, DimensionOptions> worldDimensionOptions;

	@Environment(EnvType.CLIENT)
	public final DimensionEffects worldDimensionEffects;

	public final RegistryKey<DimensionType> worldDimensionTypeRegistryKey;
	public final RegistryKey<DimensionOptions> worldDimensionOptionsRegistryKey;
	public final RegistryKey<World> worldWorldRegistryKey;

	public LiminalWorld(Identifier id, DimensionType dimensionType, Function<Long, ChunkGenerator> chunkGenerator, DimensionEffects dimensionEffects) {
		this.world = id.getPath();
		this.worldId = id;
		this.worldDimensionTypeRegistryKey = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, worldId);
		this.worldDimensionOptionsRegistryKey = RegistryKey.of(Registry.DIMENSION_KEY, worldId);
		this.worldWorldRegistryKey = RegistryKey.of(Registry.WORLD_KEY, worldId);
		this.worldDimensionType = dimensionType;
		this.worldDimensionOptions = (dimensionRegistry, seed) -> new DimensionOptions(() -> dimensionRegistry.getOrThrow(this.worldDimensionTypeRegistryKey), chunkGenerator.apply(seed));
		this.worldDimensionEffects = dimensionEffects;

	}

}
