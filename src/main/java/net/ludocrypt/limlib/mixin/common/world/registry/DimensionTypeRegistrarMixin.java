package net.ludocrypt.limlib.mixin.common.world.registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypeRegistrar;

@Mixin(DimensionTypeRegistrar.class)
public class DimensionTypeRegistrarMixin {

	@Inject(method = "Lnet/minecraft/world/dimension/DimensionTypeRegistrar;initAndGetDefault(Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/util/registry/RegistryEntry;", at = @At("RETURN"))
	private static void limlib$initAndGetDefault(Registry<DimensionType> registry, CallbackInfoReturnable<RegistryEntry<DimensionType>> ci) {
		LimlibRegistries.LIMINAL_WORLD.forEach((world) -> BuiltinRegistries.add(registry, world.getDimensionTypeKey(), world.getDimensionType()));
	}

}
