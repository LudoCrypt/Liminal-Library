package net.ludocrypt.limlib.impl.render;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.api.render.SkyHook;
import net.ludocrypt.limlib.api.render.SkyHook.RegularSky;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class LiminalSkyRendering {

	public static final HashMap<RegistryKey<World>, SkyHook> SKYBOX_REGISTRY = new HashMap<RegistryKey<World>, SkyHook>();
	public static final SkyHook DEFAULT = new RegularSky();

	public static SkyHook register(RegistryKey<World> world, SkyHook skyHook) {
		return SKYBOX_REGISTRY.put(world, skyHook);
	}

	public static SkyHook getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static SkyHook getCurrent(RegistryKey<World> key) {
		return SKYBOX_REGISTRY.getOrDefault(key, DEFAULT);
	}

}
