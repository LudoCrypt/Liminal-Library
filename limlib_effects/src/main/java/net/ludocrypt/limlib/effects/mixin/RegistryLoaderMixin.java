package net.ludocrypt.limlib.effects.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;

import net.ludocrypt.limlib.effects.post.PostEffect;
import net.ludocrypt.limlib.effects.sky.DimensionEffects;
import net.ludocrypt.limlib.effects.sound.SoundEffects;
import net.minecraft.registry.RegistryLoader;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

	@Shadow
	@Final
	@Mutable
	public static List<RegistryLoader.DecodingData<?>> WORLDGEN_REGISTRIES;

	static {
		List<RegistryLoader.DecodingData<?>> newRegistries = Lists.newArrayList();
		newRegistries.addAll(WORLDGEN_REGISTRIES);
		newRegistries.add(new RegistryLoader.DecodingData(PostEffect.POST_EFFECT_KEY, PostEffect.CODEC));
		newRegistries.add(new RegistryLoader.DecodingData(DimensionEffects.DIMENSION_EFFECTS_KEY, DimensionEffects.CODEC));
		newRegistries.add(new RegistryLoader.DecodingData(SoundEffects.SOUND_EFFECTS_KEY, SoundEffects.CODEC));
		WORLDGEN_REGISTRIES = newRegistries;
	}

}
