package net.ludocrypt.limlib.api.sound;

import java.util.Optional;

import org.lwjgl.openal.EXTEfx;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ReverbSettings {

	public static final Codec<ReverbSettings> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.BOOL.optionalFieldOf("enabled").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.enabled);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_DENSITY, EXTEfx.AL_EAXREVERB_MAX_DENSITY).optionalFieldOf("density").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.density);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_DIFFUSION, EXTEfx.AL_EAXREVERB_MAX_DIFFUSION).optionalFieldOf("diffusion").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.diffusion);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_GAIN, EXTEfx.AL_EAXREVERB_MAX_GAIN).optionalFieldOf("gain").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.gain);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_GAINHF, EXTEfx.AL_EAXREVERB_MAX_GAINHF).optionalFieldOf("gain_hf").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.gainHF);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_DECAY_TIME, EXTEfx.AL_EAXREVERB_MAX_DECAY_TIME).optionalFieldOf("decay_time").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.decayTime);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_DECAY_HFRATIO, EXTEfx.AL_EAXREVERB_MAX_DECAY_HFRATIO).optionalFieldOf("decay_hf_ratio").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.decayHFRatio);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_AIR_ABSORPTION_GAINHF, EXTEfx.AL_EAXREVERB_MAX_AIR_ABSORPTION_GAINHF).optionalFieldOf("air_absorption_gain_hf").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.airAbsorptionGainHF);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_REFLECTIONS_GAIN, EXTEfx.AL_EAXREVERB_MAX_REFLECTIONS_GAIN).optionalFieldOf("max_reflections_gain").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.reflectionsGainBase);
		}), Codec.floatRange(EXTEfx.AL_EAXREVERB_MIN_LATE_REVERB_GAIN, EXTEfx.AL_EAXREVERB_MAX_LATE_REVERB_GAIN).optionalFieldOf("late_reverb_gain").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.lateReverbGainBase);
		}), Codec.floatRange(EXTEfx.AL_REVERB_MIN_REFLECTIONS_DELAY, EXTEfx.AL_REVERB_MAX_REFLECTIONS_DELAY).optionalFieldOf("reflections_delay").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.reflectionsDelay);
		}), Codec.floatRange(EXTEfx.AL_REVERB_MIN_LATE_REVERB_DELAY, EXTEfx.AL_REVERB_MAX_LATE_REVERB_DELAY).optionalFieldOf("late_reverb_delay").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.lateReverbDelay);
		}), Codec.intRange(EXTEfx.AL_REVERB_MIN_DECAY_HFLIMIT, EXTEfx.AL_REVERB_MAX_DECAY_HFLIMIT).optionalFieldOf("decay_hf_limit").stable().forGetter((reverbSettings) -> {
			return Optional.of(reverbSettings.decayHFLimit);
		})).apply(instance, instance.stable(ReverbSettings::new));
	});

	public boolean enabled = true;
	public float density = EXTEfx.AL_EAXREVERB_DEFAULT_DENSITY;
	public float diffusion = EXTEfx.AL_EAXREVERB_DEFAULT_DIFFUSION;
	public float gain = EXTEfx.AL_EAXREVERB_DEFAULT_GAIN;
	public float gainHF = EXTEfx.AL_EAXREVERB_DEFAULT_GAINHF;
	public float decayTime = EXTEfx.AL_EAXREVERB_DEFAULT_DECAY_TIME;
	public float decayHFRatio = EXTEfx.AL_EAXREVERB_DEFAULT_DECAY_HFRATIO;
	public float airAbsorptionGainHF = EXTEfx.AL_EAXREVERB_DEFAULT_AIR_ABSORPTION_GAINHF;
	public float reflectionsGainBase = EXTEfx.AL_EAXREVERB_DEFAULT_REFLECTIONS_GAIN;
	public float lateReverbGainBase = EXTEfx.AL_EAXREVERB_DEFAULT_LATE_REVERB_GAIN;
	public float reflectionsDelay = EXTEfx.AL_REVERB_DEFAULT_REFLECTIONS_DELAY;
	public float lateReverbDelay = EXTEfx.AL_REVERB_DEFAULT_LATE_REVERB_DELAY;
	public int decayHFLimit = EXTEfx.AL_REVERB_DEFAULT_DECAY_HFLIMIT;

	public ReverbSettings() {
	}

	public ReverbSettings(boolean enabled) {
		this.enabled = enabled;
	}

	public ReverbSettings(Optional<Boolean> enabled, Optional<Float> density, Optional<Float> diffusion, Optional<Float> gain, Optional<Float> gainHF, Optional<Float> decayTime, Optional<Float> decayHFRatio, Optional<Float> airAbsorptionGainHF, Optional<Float> reflectionsGainBase, Optional<Float> lateReverbGainBase, Optional<Float> reflectionsDelay, Optional<Float> lateReverbDelay, Optional<Integer> decayHFLimit) {
		if (enabled.isPresent()) {
			this.enabled = enabled.get();
		}
		if (density.isPresent()) {
			this.density = density.get();
		}
		if (diffusion.isPresent()) {
			this.diffusion = diffusion.get();
		}
		if (gain.isPresent()) {
			this.gain = gain.get();
		}
		if (gainHF.isPresent()) {
			this.gainHF = gainHF.get();
		}
		if (decayTime.isPresent()) {
			this.decayTime = decayTime.get();
		}
		if (decayHFRatio.isPresent()) {
			this.decayHFRatio = decayHFRatio.get();
		}
		if (airAbsorptionGainHF.isPresent()) {
			this.airAbsorptionGainHF = airAbsorptionGainHF.get();
		}
		if (reflectionsGainBase.isPresent()) {
			this.reflectionsGainBase = reflectionsGainBase.get();
		}
		if (lateReverbGainBase.isPresent()) {
			this.lateReverbGainBase = lateReverbGainBase.get();
		}
		if (reflectionsDelay.isPresent()) {
			this.reflectionsDelay = reflectionsDelay.get();
		}
		if (lateReverbDelay.isPresent()) {
			this.lateReverbDelay = lateReverbDelay.get();
		}
		if (decayHFLimit.isPresent()) {
			this.decayHFLimit = decayHFLimit.get();
		}
	}

	public static boolean shouldIgnore(Identifier identifier) {
		return identifier.getPath().contains("ui.") || identifier.getPath().contains("music.") || identifier.getPath().contains("block.lava.pop") || identifier.getPath().contains("weather.") || identifier.getPath().startsWith("atmosfera") || identifier.getPath().startsWith("dynmus");
	}

	public ReverbSettings setAirAbsorptionGainHF(float airAbsorptionGainHF) {
		this.airAbsorptionGainHF = airAbsorptionGainHF;
		return this;
	}

	public ReverbSettings setDecayHFRatio(float decayHFRatio) {
		this.decayHFRatio = decayHFRatio;
		return this;
	}

	public ReverbSettings setDensity(float density) {
		this.density = density;
		return this;
	}

	public ReverbSettings setDiffusion(float diffusion) {
		this.diffusion = diffusion;
		return this;
	}

	public ReverbSettings setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public ReverbSettings setGain(float gain) {
		this.gain = gain;
		return this;
	}

	public ReverbSettings setGainHF(float gainHF) {
		this.gainHF = gainHF;
		return this;
	}

	public ReverbSettings setLateReverbGainBase(float lateReverbGainBase) {
		this.lateReverbGainBase = lateReverbGainBase;
		return this;
	}

	public ReverbSettings setDecayTime(float decayTime) {
		this.decayTime = decayTime;
		return this;
	}

	public ReverbSettings setReflectionsGainBase(float reflectionsGainBase) {
		this.reflectionsGainBase = reflectionsGainBase;
		return this;
	}

	public ReverbSettings setDecayHFLimit(int decayHFLimit) {
		this.decayHFLimit = decayHFLimit;
		return this;
	}

	public ReverbSettings setLateReverbDelay(float lateReverbDelay) {
		this.lateReverbDelay = lateReverbDelay;
		return this;
	}

	public ReverbSettings setReflectionsDelay(float reflectionsDelay) {
		this.reflectionsDelay = reflectionsDelay;
		return this;
	}

}
