package net.ludocrypt.limlib.api;

import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class LiminalWorld {

	private final Identifier identifier;
	private final DimensionType dimensionType;
	private final DimensionOptionsGetter dimensionOptionsGetter;
	private final LiminalEffects liminalEffects;

	public LiminalWorld(Identifier identifier, DimensionType dimensionType, DimensionOptionsGetter dimensionOptionsGetter, LiminalEffects liminalEffects) {
		this.identifier = identifier;
		this.dimensionType = dimensionType;
		this.dimensionOptionsGetter = dimensionOptionsGetter;
		this.liminalEffects = liminalEffects;
		((DimensionEffectsAccess) this.dimensionType).setLiminalEffects(liminalEffects);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public RegistryKey<DimensionType> getDimensionTypeKey() {
		return RegistryKey.of(Registry.DIMENSION_TYPE_KEY, identifier);
	}

	public RegistryKey<DimensionOptions> getDimensionKey() {
		return RegistryKey.of(Registry.DIMENSION_KEY, identifier);
	}

	public RegistryKey<World> getWorldKey() {
		return RegistryKey.of(Registry.WORLD_KEY, identifier);
	}

	public DimensionType getDimensionType() {
		return dimensionType;
	}

	public DimensionOptionsGetter getDimensionOptionsGetter() {
		return dimensionOptionsGetter;
	}

	public LiminalEffects getLiminalEffects() {
		return liminalEffects;
	}

	@FunctionalInterface
	public static interface DimensionOptionsGetter {
		public DimensionOptions get(LiminalWorld world, Registry<DimensionType> dimensionTypeRegistry, Registry<Biome> biomeRegistry, Registry<StructureSet> structureRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseSettingsRegistry, DynamicRegistryManager registryManager, long seed);
	}

}
