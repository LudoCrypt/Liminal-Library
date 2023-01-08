package net.ludocrypt.limlib.registry.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import net.ludocrypt.limlib.registry.registration.LimlibWorld;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.dimension.DimensionOptions;

@Mixin(targets = "net/minecraft/client/world/GeneratorTypes$Bootstrap")
public class GeneratorTypesBootstrapMixin {

	@Inject(method = "Lnet/minecraft/client/world/GeneratorTypes$Bootstrap;createType(Lnet/minecraft/world/dimension/DimensionOptions;)Lnet/minecraft/client/world/GeneratorType;", at = @At("RETURN"))
	private void limlib$createPreset(DimensionOptions dimensionOptions, CallbackInfoReturnable<GeneratorType> ci) {
		Map<RegistryKey<DimensionOptions>, DimensionOptions> map = Maps.newHashMap();
		map.putAll(((GeneratorTypeAccessor) ci.getReturnValue()).getDimensions());
		LimlibWorld.LIMLIB_WORLD.getEntries().forEach((entry) -> map.put(RegistryKey.of(RegistryKeys.DIMENSION, entry.getKey().getValue()), entry.getValue().getDimensionOptionsSupplier().get()));
		((GeneratorTypeAccessor) ci.getReturnValue()).setDimensions(map);
	}

}
