package net.ludocrypt.limlib.render.mixin.registry;

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

import net.ludocrypt.limlib.render.skybox.Skybox;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

@Mixin(DynamicRegistryManager.class)
public interface DynamicRegistryManagerMixin {

	@Inject(method = "Lnet/minecraft/util/registry/DynamicRegistryManager;method_30531()Lcom/google/common/collect/ImmutableMap;", at = @At(value = "RETURN", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$addRegistries(CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>>> ci, Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> builder) {
		register(builder, Skybox.SKYBOX_KEY, Skybox.CODEC);
		register(builder, SpecialModelRenderer.SPECIAL_MODEL_RENDERER_KEY, SpecialModelRenderer.CODEC);
	}

	@Shadow
	private static <E> void register(Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> infosBuilder, RegistryKey<? extends Registry<E>> registryRef, Codec<E> entryCodec) {
		throw new UnsupportedOperationException();
	}

}
