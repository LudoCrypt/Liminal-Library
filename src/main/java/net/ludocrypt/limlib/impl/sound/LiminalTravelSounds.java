package net.ludocrypt.limlib.impl.sound;

import java.util.HashMap;

import net.ludocrypt.limlib.api.sound.LiminalTravelSound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class LiminalTravelSounds {

	public static final HashMap<RegistryKey<World>, LiminalTravelSound> TRAVEL_SOUND_REGISTRY = new HashMap<RegistryKey<World>, LiminalTravelSound>();
	public static final LiminalTravelSound DEFAULT = new LiminalTravelSound.SimpleTravelSound(SoundEvents.BLOCK_PORTAL_TRAVEL);

	public static LiminalTravelSound register(RegistryKey<World> world, LiminalTravelSound travelSound) {
		return TRAVEL_SOUND_REGISTRY.put(world, travelSound);
	}

	public static LiminalTravelSound getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static LiminalTravelSound getCurrent(RegistryKey<World> key) {
		return TRAVEL_SOUND_REGISTRY.getOrDefault(key, DEFAULT);
	}

}
