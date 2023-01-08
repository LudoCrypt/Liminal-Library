package net.ludocrypt.limlib.effects;

import java.util.Optional;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import net.ludocrypt.limlib.effects.post.EmptyPostEffect;
import net.ludocrypt.limlib.effects.post.PostEffect;
import net.ludocrypt.limlib.effects.post.StaticPostEffect;
import net.ludocrypt.limlib.effects.sky.DimensionEffects;
import net.ludocrypt.limlib.effects.sky.EmptyDimensionEffects;
import net.ludocrypt.limlib.effects.sky.StaticDimensionEffects;
import net.ludocrypt.limlib.effects.sound.distortion.DistortionEffect;
import net.ludocrypt.limlib.effects.sound.distortion.StaticDistortionEffect;
import net.ludocrypt.limlib.effects.sound.reverb.ReverbEffect;
import net.ludocrypt.limlib.effects.sound.reverb.StaticReverbEffect;
import net.minecraft.registry.Holder;
import net.minecraft.registry.HolderLookup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class LimlibEffects implements ModInitializer {

	@Override
	public void onInitialize(ModContainer mod) {
		Registry.register(ReverbEffect.REVERB_EFFECT_CODEC, new Identifier("limlib", "static"), StaticReverbEffect.CODEC);
		Registry.register(DistortionEffect.DISTORTION_EFFECT_CODEC, new Identifier("limlib", "static"), StaticDistortionEffect.CODEC);

		Registry.register(DimensionEffects.DIMENSION_EFFECTS_CODEC, new Identifier("limlib", "static"), StaticDimensionEffects.CODEC);
		Registry.register(DimensionEffects.DIMENSION_EFFECTS_CODEC, new Identifier("limlib", "empty"), EmptyDimensionEffects.CODEC);

		Registry.register(PostEffect.POST_EFFECT_CODEC, new Identifier("limlib", "static"), StaticPostEffect.CODEC);
		Registry.register(PostEffect.POST_EFFECT_CODEC, new Identifier("limlib", "empty"), EmptyPostEffect.CODEC);
	}

	public static <T> Optional<T> snatch(HolderLookup<T> lookup, RegistryKey<T> key) {
		Optional<Holder.Reference<T>> holderOptional = lookup.getHolder(key);

		if (holderOptional.isPresent()) {
			Holder.Reference<T> holder = holderOptional.get();
			try {
				T held = holder.value();
				return Optional.of(held);
			} catch (IllegalStateException e) {
				return Optional.empty();
			}
		}

		return Optional.empty();
	}

}
