package net.ludocrypt.limlib.impl;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ludocrypt.limlib.api.render.LiminalShader;
import net.ludocrypt.limlib.api.render.SkyHook;
import net.ludocrypt.limlib.api.sound.LiminalTravelSound;
import net.ludocrypt.limlib.api.sound.ReverbSettings;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.math.Vec3d;

public class LiminalEffects {

	public static final Codec<DimensionEffects> DIMENSION_EFFECTS_CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.FLOAT.optionalFieldOf("cloud_height").stable().forGetter((sky) -> {
			return sky.getCloudsHeight() == Float.NaN ? Optional.empty() : Optional.of(sky.getCloudsHeight());
		}), Codec.BOOL.fieldOf("alternate_sky_color").stable().forGetter((sky) -> {
			return sky.isAlternateSkyColor();
		}), Codec.STRING.fieldOf("sky_type").stable().forGetter((sky) -> {
			return sky.getSkyType().name();
		}), Codec.BOOL.fieldOf("brighten_lighting").stable().forGetter((sky) -> {
			return sky.shouldBrightenLighting();
		}), Codec.BOOL.fieldOf("darkened").stable().forGetter((sky) -> {
			return sky.isDarkened();
		}), Codec.BOOL.fieldOf("thick_fog").stable().forGetter((sky) -> {
			return sky.useThickFog(0, 0);
		})).apply(instance, instance.stable((cloudHeight, alternateSkyColor, skyType, brightenLighting, darkened, thickFog) -> {
			return new DimensionEffects(cloudHeight.orElse(Float.NaN), alternateSkyColor, DimensionEffects.SkyType.valueOf(skyType), brightenLighting, darkened) {

				@Override
				public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
					return color;
				}

				@Override
				public boolean useThickFog(int camX, int camY) {
					return thickFog;
				}

			};
		}));
	});

	public static final Codec<LiminalEffects> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(DIMENSION_EFFECTS_CODEC.optionalFieldOf("dimension_effects").stable().forGetter((liminalEffects) -> {
			return liminalEffects.getEffects();
		}), LiminalShader.CODEC.optionalFieldOf("shader").stable().forGetter((liminalEffects) -> {
			return liminalEffects.getShader();
		}), SkyHook.CODEC.optionalFieldOf("sky").stable().forGetter((liminalEffects) -> {
			return liminalEffects.getSky();
		}), LiminalTravelSound.CODEC.optionalFieldOf("travel_sound").stable().forGetter((liminalEffects) -> {
			return liminalEffects.getTravel();
		}), MusicSound.CODEC.optionalFieldOf("music").stable().forGetter((liminalEffects) -> {
			return liminalEffects.getMusic();
		}), ReverbSettings.CODEC.optionalFieldOf("reverb").stable().forGetter((liminalEffects) -> {
			return liminalEffects.getReverb();
		})).apply(instance, instance.stable(LiminalEffects::new));
	});

	private Optional<DimensionEffects> effects;
	private Optional<LiminalShader> shader;
	private Optional<SkyHook> sky;
	private Optional<LiminalTravelSound> travel;
	private Optional<MusicSound> music;
	private Optional<ReverbSettings> reverb;

	public LiminalEffects(Optional<DimensionEffects> effects, Optional<LiminalShader> shader, Optional<SkyHook> sky, Optional<LiminalTravelSound> travel, Optional<MusicSound> music, Optional<ReverbSettings> reverb) {
		this.effects = effects;
		this.shader = shader;
		this.sky = sky;
		this.travel = travel;
		this.music = music;
		this.reverb = reverb;
	}

	public void setEffects(Optional<DimensionEffects> effects) {
		this.effects = effects;
	}

	public void setMusic(Optional<MusicSound> music) {
		this.music = music;
	}

	public void setReverb(Optional<ReverbSettings> reverb) {
		this.reverb = reverb;
	}

	public void setShader(Optional<LiminalShader> shader) {
		this.shader = shader;
	}

	public void setSky(Optional<SkyHook> sky) {
		this.sky = sky;
	}

	public void setTravel(Optional<LiminalTravelSound> travel) {
		this.travel = travel;
	}

	public Optional<DimensionEffects> getEffects() {
		return effects;
	}

	public Optional<MusicSound> getMusic() {
		return music;
	}

	public Optional<ReverbSettings> getReverb() {
		return reverb;
	}

	public Optional<LiminalShader> getShader() {
		return shader;
	}

	public Optional<SkyHook> getSky() {
		return sky;
	}

	public Optional<LiminalTravelSound> getTravel() {
		return travel;
	}

}
