package net.ludocrypt.limlib.effects.sound;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ludocrypt.limlib.effects.mixin.BuiltinRegistriesAccessor;
import net.ludocrypt.limlib.effects.mixin.RegistryAccessor;
import net.ludocrypt.limlib.effects.sound.distortion.DistortionEffect;
import net.ludocrypt.limlib.effects.sound.reverb.ReverbEffect;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class SoundEffects {

	public static final RegistryKey<Registry<SoundEffects>> SOUND_EFFECTS_KEY = RegistryAccessor.callCreateRegistryKey("limlib/sound_effects");
	public static final Registry<SoundEffects> SOUND_EFFECTS = BuiltinRegistriesAccessor.callAddRegistry(SOUND_EFFECTS_KEY, (registry) -> BuiltinRegistries.register(registry, RegistryKey.of(SOUND_EFFECTS_KEY, new Identifier("limlib", "default")), new SoundEffects()));

	public static final Codec<SoundEffects> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(ReverbEffect.CODEC.optionalFieldOf("reverb").stable().forGetter((soundEffects) -> {
			return soundEffects.reverb;
		}), DistortionEffect.CODEC.optionalFieldOf("distortion").stable().forGetter((soundEffects) -> {
			return soundEffects.distortion;
		}), MusicSound.CODEC.optionalFieldOf("music").stable().forGetter((soundEffects) -> {
			return soundEffects.music;
		})).apply(instance, instance.stable(SoundEffects::new));
	});

	private final Optional<ReverbEffect> reverb;
	private final Optional<DistortionEffect> distortion;
	private final Optional<MusicSound> music;

	public SoundEffects() {
		this(Optional.empty(), Optional.empty(), Optional.empty());
	}

	public SoundEffects(Optional<ReverbEffect> reverb, Optional<DistortionEffect> distortion, Optional<MusicSound> music) {
		this.reverb = reverb;
		this.distortion = distortion;
		this.music = music;
	}

	public Optional<ReverbEffect> getReverb() {
		return reverb;
	}

	public Optional<DistortionEffect> getDistortion() {
		return distortion;
	}

	public Optional<MusicSound> getMusic() {
		return music;
	}

}
