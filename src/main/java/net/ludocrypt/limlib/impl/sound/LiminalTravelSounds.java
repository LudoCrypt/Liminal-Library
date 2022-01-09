package net.ludocrypt.limlib.impl.sound;

import java.util.HashMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class LiminalTravelSounds {

	public static boolean isChangingDimension = false;

	public static final HashMap<RegistryKey<World>, SoundEvent> TRAVEL_SOUND_REGISTRY = new HashMap<RegistryKey<World>, SoundEvent>();
	public static final SoundEvent DEFAULT = SoundEvents.BLOCK_PORTAL_TRAVEL;

	public static SoundEvent register(RegistryKey<World> world, SoundEvent travelSound) {
		return TRAVEL_SOUND_REGISTRY.put(world, travelSound);
	}

	public static SoundEvent getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static SoundEvent getCurrent(RegistryKey<World> key) {
		return TRAVEL_SOUND_REGISTRY.getOrDefault(key, DEFAULT);
	}

}
