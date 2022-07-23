package net.ludocrypt.limlib.mixin.common.world.registry;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;

@Mixin(targets = "net/minecraft/world/gen/WorldPresets$Registrar")
public class WorldPresetsRegistrarMixin {

	@Inject(method = "Lnet/minecraft/world/gen/WorldPresets$Registrar;createPreset(Lnet/minecraft/world/dimension/DimensionOptions;)Lnet/minecraft/world/gen/WorldPreset;", at = @At("RETURN"))
	private void limlib$CreatePreset(DimensionOptions dimensionOptions, CallbackInfoReturnable<WorldPreset> ci) {
		Map<RegistryKey<DimensionOptions>, DimensionOptions> map = Maps.newHashMap();
		map.putAll(((WorldPresetAccessor) ci.getReturnValue()).getDimensions());
		LimlibRegistries.LIMINAL_WORLD.forEach((world) -> map.put(world.getDimensionKey(), world.getDimensionOptions()));
		((WorldPresetAccessor) ci.getReturnValue()).setDimensions(map);
	}

}
