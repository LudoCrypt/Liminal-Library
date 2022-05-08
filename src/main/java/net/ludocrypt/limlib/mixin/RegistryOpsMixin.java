package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.DynamicOps;

import net.ludocrypt.limlib.impl.LevelStorageHacks;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;

@Mixin(RegistryOps.class)
public class RegistryOpsMixin {

	@Inject(method = "Lnet/minecraft/util/dynamic/RegistryOps;ofLoaded(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/util/registry/DynamicRegistryManager$Mutable;Lnet/minecraft/resource/ResourceManager;)Lnet/minecraft/util/dynamic/RegistryOps;", at = @At("RETURN"))
	private static <T> void limlib$registryHacks(DynamicOps<T> ops, DynamicRegistryManager.Mutable registryManager, ResourceManager resourceManager, CallbackInfoReturnable<RegistryOps<T>> ci) {
		LevelStorageHacks.earlyDynamicRegistryManager = registryManager;
	}
}
