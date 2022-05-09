package net.ludocrypt.limlib.api.render;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.DimensionEffects.SkyType;
import net.minecraft.util.math.Vec3d;

public abstract class LiminalBaseEffects {

	public static final Codec<LiminalBaseEffects> CODEC = LimlibRegistries.LIMINAL_BASE_EFFECTS.getCodec().dispatchStable(LiminalBaseEffects::getCodec, Function.identity());

	public abstract Codec<? extends LiminalBaseEffects> getCodec();

	@Environment(EnvType.CLIENT)
	public abstract DimensionEffects getDimensionEffects();

	public static class SimpleBaseEffects extends LiminalBaseEffects {

		public static final Codec<SimpleBaseEffects> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(Codec.FLOAT.optionalFieldOf("cloud_height").stable().forGetter((effects) -> {
				return effects.cloudsHeight;
			}), Codec.BOOL.fieldOf("alternate_sky_color").stable().forGetter((effects) -> {
				return effects.alternateSkyColor;
			}), Codec.STRING.fieldOf("sky_type").stable().forGetter((effects) -> {
				return effects.skyType;
			}), Codec.BOOL.fieldOf("brighten_lighting").stable().forGetter((effects) -> {
				return effects.brightenLighting;
			}), Codec.BOOL.fieldOf("darkened").stable().forGetter((effects) -> {
				return effects.darkened;
			}), Codec.BOOL.fieldOf("thick_fog").stable().forGetter((effects) -> {
				return effects.thickFog;
			})).apply(instance, instance.stable(SimpleBaseEffects::new));
		});

		private final Optional<Float> cloudsHeight;
		private final boolean alternateSkyColor;
		private final String skyType;
		private final boolean brightenLighting;
		private final boolean darkened;
		private final boolean thickFog;

		@Environment(EnvType.CLIENT)
		private DimensionEffects dimensionEffects = null;

		public SimpleBaseEffects(Optional<Float> cloudsHeight, boolean alternateSkyColor, String skyType, boolean brightenLighting, boolean darkened, boolean thickFog) {
			this.cloudsHeight = cloudsHeight;
			this.alternateSkyColor = alternateSkyColor;
			this.skyType = skyType;
			this.brightenLighting = brightenLighting;
			this.darkened = darkened;
			this.thickFog = thickFog;
		}

		@Override
		public Codec<? extends LiminalBaseEffects> getCodec() {
			return CODEC;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public DimensionEffects getDimensionEffects() {
			if (this.dimensionEffects == null) {
				this.dimensionEffects = new DimensionEffects(cloudsHeight.orElse(Float.NaN), alternateSkyColor, SkyType.valueOf(skyType), brightenLighting, darkened) {

					@Override
					public Vec3d adjustFogColor(Vec3d color, float angle) {
						return color;
					}

					@Override
					public boolean useThickFog(int var1, int var2) {
						return SimpleBaseEffects.this.thickFog;
					}

				};
			}

			return this.dimensionEffects;

		}

	}

}
