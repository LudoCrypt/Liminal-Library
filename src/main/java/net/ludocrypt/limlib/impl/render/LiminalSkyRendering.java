package net.ludocrypt.limlib.impl.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.render.SkyHook;
import net.ludocrypt.limlib.api.render.SkyHook.RegularSky;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class LiminalSkyRendering {

	public static final SimpleRegistry<SkyHook> LIMINAL_SKY_REGISTRY = FabricRegistryBuilder.createDefaulted(SkyHook.class, new Identifier("limlib", "sky_registry"), new Identifier("limlib", "regular_sky")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SkyHook DEFAULT = new RegularSky();

	public static void init() {
		Registry.register(LIMINAL_SKY_REGISTRY, new Identifier("limlib", "regular_sky"), DEFAULT);
	}

	public static SkyHook register(RegistryKey<World> world, SkyHook reverb) {
		return Registry.register(LIMINAL_SKY_REGISTRY, world.getValue(), reverb);
	}

	public static SkyHook getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static SkyHook getCurrent(RegistryKey<World> key) {
		return LIMINAL_SKY_REGISTRY.getOrEmpty(key.getValue()).orElse(DEFAULT);
	}

}
