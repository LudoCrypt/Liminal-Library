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

import net.ludocrypt.limlib.effects.post.PostEffect;
import net.ludocrypt.limlib.effects.sky.DimensionEffects;
import net.ludocrypt.limlib.effects.sound.SoundEffects;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.unmapped.C_uhbbwvga;

@Mixin(C_uhbbwvga.class)
public abstract class ServerRegistryMixin {

	@Inject(method = "Lnet/minecraft/unmapped/C_uhbbwvga;m_moxntmzr()Lcom/google/common/collect/ImmutableMap;", at = @At(value = "INVOKE", target = "Lnet/minecraft/unmapped/C_uhbbwvga;m_dfuklmpj(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Codec;)V", ordinal = 2, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$makeMap$mapped(CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, C_uhbbwvga.C_keyyzmde<?>>> ci, Builder<RegistryKey<? extends Registry<?>>, C_uhbbwvga.C_keyyzmde<?>> builder) {
		m_dfuklmpj(builder, PostEffect.POST_EFFECT_KEY, PostEffect.CODEC);
		m_dfuklmpj(builder, DimensionEffects.DIMENSION_EFFECTS_KEY, DimensionEffects.CODEC);
		m_dfuklmpj(builder, SoundEffects.SOUND_EFFECTS_KEY, SoundEffects.CODEC);
	}

	@Shadow
	private native static <E> void m_dfuklmpj(Builder<RegistryKey<? extends Registry<?>>, C_uhbbwvga.C_keyyzmde<?>> builder, RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec);
}
