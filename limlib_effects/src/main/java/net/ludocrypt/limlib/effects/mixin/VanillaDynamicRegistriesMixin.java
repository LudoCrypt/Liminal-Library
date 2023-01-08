package net.ludocrypt.limlib.effects.mixin;

import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.effects.post.PostEffect;
import net.ludocrypt.limlib.effects.post.PostEffectBootstrap;
import net.ludocrypt.limlib.effects.sky.DimensionEffects;
import net.ludocrypt.limlib.effects.sky.DimensionEffectsBootstrap;
import net.ludocrypt.limlib.effects.sound.SoundEffects;
import net.ludocrypt.limlib.effects.sound.SoundEffectsBootstrap;
import net.minecraft.registry.RegistrySetBuilder;
import net.minecraft.registry.VanillaDynamicRegistries;

@Mixin(VanillaDynamicRegistries.class)
public class VanillaDynamicRegistriesMixin {

	@Shadow
	@Final
	private static RegistrySetBuilder BUILDER;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void limlib$clinit(CallbackInfo ci) {
		BUILDER.add(PostEffect.POST_EFFECT_KEY, (context) -> QuiltLoader.getEntrypoints(PostEffectBootstrap.ENTRYPOINT_KEY, PostEffectBootstrap.class).forEach((bootstrap) -> bootstrap.register(context)));
		BUILDER.add(DimensionEffects.DIMENSION_EFFECTS_KEY, (context) -> QuiltLoader.getEntrypoints(DimensionEffectsBootstrap.ENTRYPOINT_KEY, DimensionEffectsBootstrap.class).forEach((bootstrap) -> bootstrap.register(context)));
		BUILDER.add(SoundEffects.SOUND_EFFECTS_KEY, (context) -> QuiltLoader.getEntrypoints(SoundEffectsBootstrap.ENTRYPOINT_KEY, SoundEffectsBootstrap.class).forEach((bootstrap) -> bootstrap.register(context)));
	}

}
