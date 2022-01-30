package net.ludocrypt.limlib.impl.sound;

import java.util.HashMap;
import java.util.Optional;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.api.world.LiminalWorld;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class LiminalWorldMusic {

	public static final HashMap<RegistryKey<World>, Optional<MusicSound>> WORLD_MUSIC_REGISTRY = new HashMap<RegistryKey<World>, Optional<MusicSound>>();
	public static final Optional<MusicSound> DEFAULT = Optional.empty();

	public static Optional<MusicSound> register(LiminalWorld world, Optional<MusicSound> music) {
		return register(world.worldWorldRegistryKey, music);
	}

	public static Optional<MusicSound> register(RegistryKey<World> world, Optional<MusicSound> music) {
		return WORLD_MUSIC_REGISTRY.put(world, music);
	}

	public static Optional<MusicSound> getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static Optional<MusicSound> getCurrent(RegistryKey<World> key) {
		return WORLD_MUSIC_REGISTRY.getOrDefault(key, DEFAULT);
	}

}
