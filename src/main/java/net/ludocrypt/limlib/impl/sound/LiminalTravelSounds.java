package net.ludocrypt.limlib.impl.sound;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;

public class LiminalTravelSounds {

	public static boolean isChangingDimension = false;

	public static final SimpleRegistry<SoundEvent> TRAVEL_SOUND_REGISTRY = FabricRegistryBuilder.createDefaulted(SoundEvent.class, new Identifier("limlib", "travel_sound"), new Identifier("limlib", "default_travel")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SoundEvent DEFAULT = SoundEvents.BLOCK_PORTAL_TRAVEL;

	public static void init() {
		Registry.register(TRAVEL_SOUND_REGISTRY, new Identifier("limlib", "default_travel"), DEFAULT);
	}

	public static SoundEvent register(RegistryKey<World> world, SoundEvent reverb) {
		return Registry.register(TRAVEL_SOUND_REGISTRY, world.getValue(), reverb);
	}

	public static SoundEvent getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static SoundEvent getCurrent(RegistryKey<World> key) {
		return TRAVEL_SOUND_REGISTRY.getOrEmpty(key.getValue()).orElse(DEFAULT);
	}

}
