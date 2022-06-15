package net.ludocrypt.limlib.mixin;

import java.util.Optional;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.ludocrypt.limlib.api.render.LiminalBaseEffects;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Util;
import net.minecraft.world.dimension.DimensionType;

@Environment(EnvType.CLIENT)
@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {

	@Unique
	private static Function<DimensionType, Optional<DimensionEffects>> limlibEffects = Util.memoize((type) -> ((DimensionEffectsAccess) (Object) type).getLiminalEffects().getEffects().map(LiminalBaseEffects::getDimensionEffects));

	@Inject(method = "byDimensionType", at = @At("HEAD"), cancellable = true)
	private static void limlib$byDimensionType(DimensionType dimensionType, CallbackInfoReturnable<DimensionEffects> ci) {
		limlibEffects.apply(dimensionType).ifPresent(ci::setReturnValue);
	}

}
