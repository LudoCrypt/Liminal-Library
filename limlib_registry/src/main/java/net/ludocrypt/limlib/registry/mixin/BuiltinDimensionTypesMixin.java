package net.ludocrypt.limlib.registry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.registry.registration.LimlibWorld;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager.RegistryEntry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.BuiltinDimensionTypes;
import net.minecraft.world.dimension.DimensionType;

@Mixin(BuiltinDimensionTypes.class)
public class BuiltinDimensionTypesMixin {

	@Inject(method = "Lnet/minecraft/world/dimension/BuiltinDimensionTypes;bootstrap(Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/util/Holder;", at = @At("RETURN"))
	private static void limlib$initAndGetDefault(Registry<DimensionType> registry, CallbackInfoReturnable<RegistryEntry<DimensionType>> ci) {
		LimlibWorld.LIMLIB_WORLD.getEntries().forEach((entry) -> BuiltinRegistries.register(registry, RegistryKey.of(Registry.DIMENSION_TYPE_KEY, entry.getKey().getValue()), entry.getValue().getDimensionTypeSupplier().get()));
	}

}
