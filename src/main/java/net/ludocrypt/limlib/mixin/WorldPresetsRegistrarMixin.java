package net.ludocrypt.limlib.mixin;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;

@Mixin(targets = { "net/minecraft/world/gen/WorldPresets$Registrar" })
public abstract class WorldPresetsRegistrarMixin {

	@Inject(method = "createPreset", at = @At("RETURN"), cancellable = true)
	private void limlib$createPreset(DimensionOptions options, CallbackInfoReturnable<WorldPreset> ci) {
		HashMap<RegistryKey<DimensionOptions>, DimensionOptions> map = Maps.newHashMap();
		map.putAll(((WorldPresetAccessor) ci.getReturnValue()).getDimensions());
		LimlibRegistries.LIMINAL_WORLD.forEach((world) -> map.put(world.getDimensionKey(), world.getDimensionOptionsGetter().get(world, BuiltinRegistries.DIMENSION_TYPE, BuiltinRegistries.BIOME, BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.CHUNK_GENERATOR_SETTINGS, BuiltinRegistries.NOISE_PARAMETERS)));
		((WorldPresetAccessor) ci.getReturnValue()).setDimensions(map);
	}

}
