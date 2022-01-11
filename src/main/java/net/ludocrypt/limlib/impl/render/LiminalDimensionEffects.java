package net.ludocrypt.limlib.impl.render;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.api.world.LiminalWorld;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class LiminalDimensionEffects {

	public static final HashMap<RegistryKey<World>, DimensionEffects> DIMENSION_EFFECTS_REGISTRY = new HashMap<RegistryKey<World>, DimensionEffects>();
	public static final DimensionEffects DEFAULT = new DimensionEffects.Overworld();

	public static DimensionEffects register(LiminalWorld world, DimensionEffects dimensionEffects) {
		return register(world.worldWorldRegistryKey, dimensionEffects);
	}

	public static DimensionEffects register(RegistryKey<World> world, DimensionEffects DimensionEffects) {
		return DIMENSION_EFFECTS_REGISTRY.put(world, DimensionEffects);
	}

	public static DimensionEffects getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static DimensionEffects getCurrent(RegistryKey<World> key) {
		return DIMENSION_EFFECTS_REGISTRY.getOrDefault(key, DEFAULT);
	}

}
