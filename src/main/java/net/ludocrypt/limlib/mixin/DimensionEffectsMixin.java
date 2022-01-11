package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.impl.render.LiminalDimensionEffects;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

@Environment(EnvType.CLIENT)
@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {

	@Inject(method = "byDimensionType", at = @At("HEAD"), cancellable = true)
	private static void limlib$byDimensionType(DimensionType dimensionType, CallbackInfoReturnable<DimensionEffects> ci) {
		DimensionEffects limlibEffect = LiminalDimensionEffects.DIMENSION_EFFECTS_REGISTRY.get(RegistryKey.of(Registry.WORLD_KEY, dimensionType.getEffects()));
		if (limlibEffect != null) {
			ci.setReturnValue(limlibEffect);
		}
	}

}
