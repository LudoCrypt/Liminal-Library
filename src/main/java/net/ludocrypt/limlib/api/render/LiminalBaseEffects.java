package net.ludocrypt.limlib.api.render;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

public abstract class LiminalBaseEffects extends DimensionEffects {

	public static final Codec<LiminalBaseEffects> CODEC = LimlibRegistries.LIMINAL_BASE_EFFECTS.getCodec().dispatchStable(LiminalBaseEffects::getCodec, Function.identity());

	public LiminalBaseEffects(float cloudsHeight, boolean alternateSkyColor, SkyType skyType, boolean brightenLighting, boolean darkened) {
		super(cloudsHeight, alternateSkyColor, skyType, brightenLighting, darkened);
	}

	public abstract Codec<? extends LiminalBaseEffects> getCodec();

	public static class SimpleBaseEffects extends LiminalBaseEffects {

		public static final Codec<SimpleBaseEffects> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(Codec.FLOAT.optionalFieldOf("cloud_height").stable().forGetter((effects) -> {
				return effects.getCloudsHeight() == Float.NaN ? Optional.empty() : Optional.of(effects.getCloudsHeight());
			}), Codec.BOOL.fieldOf("alternate_sky_color").stable().forGetter((effects) -> {
				return effects.isAlternateSkyColor();
			}), Codec.STRING.fieldOf("sky_type").stable().forGetter((effects) -> {
				return effects.getSkyType().name();
			}), Codec.BOOL.fieldOf("brighten_lighting").stable().forGetter((effects) -> {
				return effects.shouldBrightenLighting();
			}), Codec.BOOL.fieldOf("darkened").stable().forGetter((effects) -> {
				return effects.isDarkened();
			}), Codec.BOOL.fieldOf("thick_fog").stable().forGetter((effects) -> {
				return effects.thickFog;
			})).apply(instance, instance.stable(SimpleBaseEffects::new));
		});

		private boolean thickFog;

		public SimpleBaseEffects(Optional<Float> cloudsHeight, boolean alternateSkyColor, String skyType, boolean brightenLighting, boolean darkened, boolean thickFog) {
			this(cloudsHeight, alternateSkyColor, SkyType.valueOf(skyType), brightenLighting, darkened, thickFog);
		}

		public SimpleBaseEffects(Optional<Float> cloudsHeight, boolean alternateSkyColor, SkyType skyType, boolean brightenLighting, boolean darkened, boolean thickFog) {
			super(cloudsHeight.orElseGet(() -> Float.NaN), alternateSkyColor, skyType, brightenLighting, darkened);
			this.thickFog = thickFog;
		}

		@Override
		public Codec<? extends LiminalBaseEffects> getCodec() {
			return CODEC;
		}

		@Override
		public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
			return color;
		}

		@Override
		public boolean useThickFog(int camX, int camY) {
			return thickFog;
		}

	}

}
