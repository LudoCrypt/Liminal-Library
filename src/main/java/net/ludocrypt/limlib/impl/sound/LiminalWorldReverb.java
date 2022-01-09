package net.ludocrypt.limlib.impl.sound;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.api.sound.ReverbSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class LiminalWorldReverb {

	public static final HashMap<RegistryKey<World>, ReverbSettings> REVERB_REGISTRY = new HashMap<RegistryKey<World>, ReverbSettings>();
	public static final ReverbSettings DEFAULT = new ReverbSettings(false);

	public static ReverbSettings register(RegistryKey<World> world, ReverbSettings reverb) {
		return REVERB_REGISTRY.put(world, reverb);
	}

	public static ReverbSettings getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static ReverbSettings getCurrent(RegistryKey<World> key) {
		return REVERB_REGISTRY.getOrDefault(key, DEFAULT);
	}

}
