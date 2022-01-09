package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.serialization.Lifecycle;

import net.ludocrypt.limlib.impl.world.LiminalDimensions;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {

	@Inject(method = "addRegistryDefaults", at = @At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$addRegistryDefaults(DynamicRegistryManager registryManager, CallbackInfoReturnable<DynamicRegistryManager> ci, MutableRegistry<DimensionType> mutableRegistry) {
		LiminalDimensions.LIMINAL_WORLD_REGISTRY.forEach((world) -> mutableRegistry.add(world.worldDimensionTypeRegistryKey, world.worldDimensionType, Lifecycle.stable()));
	}

	@Inject(method = "Lnet/minecraft/world/dimension/DimensionType;createDefaultDimensionOptions(Lnet/minecraft/util/registry/DynamicRegistryManager;JZ)Lnet/minecraft/util/registry/SimpleRegistry;", at = @At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$createDefaultDimensionOptions(DynamicRegistryManager registryManager, long seed, boolean bl, CallbackInfoReturnable<SimpleRegistry<DimensionOptions>> ci, SimpleRegistry<DimensionOptions> simpleRegistry, Registry<DimensionType> dimensionRegistry) {
		LiminalDimensions.LIMINAL_WORLD_REGISTRY.forEach((world) -> simpleRegistry.add(world.worldDimensionOptionsRegistryKey, world.worldDimensionOptions.apply(dimensionRegistry, seed), Lifecycle.stable()));
	}

}
