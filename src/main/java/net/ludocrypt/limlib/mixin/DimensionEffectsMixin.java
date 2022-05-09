package net.ludocrypt.limlib.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.ludocrypt.limlib.api.render.LiminalBaseEffects;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.world.dimension.DimensionType;

@Environment(EnvType.CLIENT)
@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {

	@Inject(method = "byDimensionType", at = @At("HEAD"), cancellable = true)
	private static void limlib$byDimensionType(DimensionType dimensionType, CallbackInfoReturnable<DimensionEffects> ci) {
		Optional<LiminalBaseEffects> limlibEffect = ((DimensionEffectsAccess) dimensionType).getLiminalEffects().getEffects();
		if (limlibEffect.isPresent()) {
			ci.setReturnValue(limlibEffect.get().getDimensionEffects());
		}
	}

}
