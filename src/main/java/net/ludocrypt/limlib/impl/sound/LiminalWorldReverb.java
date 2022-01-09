package net.ludocrypt.limlib.impl.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.sound.ReverbSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class LiminalWorldReverb {

	public static final SimpleRegistry<ReverbSettings> REVERB_REGISTRY = FabricRegistryBuilder.createDefaulted(ReverbSettings.class, new Identifier("limlib", "reverb_registry"), new Identifier("limlib", "default_reverb")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final ReverbSettings DEFAULT = new ReverbSettings(false);

	public static void init() {
		Registry.register(REVERB_REGISTRY, new Identifier("limlib", "default_reverb"), DEFAULT);
	}

	public static ReverbSettings register(RegistryKey<World> world, ReverbSettings reverb) {
		return Registry.register(REVERB_REGISTRY, world.getValue(), reverb);
	}

	public static ReverbSettings getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static ReverbSettings getCurrent(RegistryKey<World> key) {
		return REVERB_REGISTRY.getOrEmpty(key.getValue()).orElse(DEFAULT);
	}

}
