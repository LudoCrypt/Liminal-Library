package net.ludocrypt.limlib.effects.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Codec;

import net.ludocrypt.limlib.effects.render.post.PostEffect;
import net.ludocrypt.limlib.effects.render.sky.SkyEffects;
import net.ludocrypt.limlib.effects.sound.SoundEffects;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

@Mixin(DynamicRegistryManager.class)
public interface DynamicRegistryManagerMixin {

	@Inject(method = "Lnet/minecraft/util/registry/DynamicRegistryManager;method_30531()Lcom/google/common/collect/ImmutableMap;", at = @At(value = "RETURN", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$addRegistries(CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>>> ci, Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> builder) {
		register(builder, SoundEffects.SOUND_EFFECTS_KEY, SoundEffects.CODEC);
		register(builder, SkyEffects.SKY_EFFECTS_KEY, SkyEffects.CODEC);
		register(builder, PostEffect.POST_EFFECT_KEY, PostEffect.CODEC);
	}

	@Shadow
	private static <E> void register(Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> infosBuilder, RegistryKey<? extends Registry<E>> registryRef, Codec<E> entryCodec) {
		throw new UnsupportedOperationException();
	}

}
