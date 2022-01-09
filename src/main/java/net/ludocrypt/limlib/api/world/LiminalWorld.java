package net.ludocrypt.limlib.api.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class LiminalWorld {

	public final String world;
	public final Identifier worldId;
	public final DimensionType worldDimensionType;
	public final DimensionOptions worldDimensionOptions;

	@Environment(EnvType.CLIENT)
	public final DimensionEffects worldDimensionEffects;

	public final RegistryKey<DimensionType> worldDimensionTypeRegistryKey;
	public final RegistryKey<DimensionOptions> worldDimensionOptionsRegistryKey;
	public final RegistryKey<World> worldWorldRegistryKey;

	public LiminalWorld(Identifier id, DimensionType dimensionType, DimensionOptions dimensionOptions, DimensionEffects dimensionEffects) {
		this.world = id.getPath();
		this.worldId = id;
		this.worldDimensionType = dimensionType;
		this.worldDimensionOptions = dimensionOptions;
		this.worldDimensionEffects = dimensionEffects;
		this.worldDimensionTypeRegistryKey = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, worldId);
		this.worldDimensionOptionsRegistryKey = RegistryKey.of(Registry.DIMENSION_KEY, worldId);
		this.worldWorldRegistryKey = RegistryKey.of(Registry.WORLD_KEY, worldId);
	}

}
