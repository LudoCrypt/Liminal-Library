package net.ludocrypt.limlib.effects.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.effects.render.sky.SkyEffects;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

@Mixin(SkyProperties.class)
public class SkyPropertiesMixin {

	@Inject(method = "Lnet/minecraft/client/render/SkyProperties;byDimensionType(Lnet/minecraft/world/dimension/DimensionType;)Lnet/minecraft/client/render/SkyProperties;", at = @At("HEAD"), cancellable = true)
	private static void limlib$byDimensionType(DimensionType dimensionType, CallbackInfoReturnable<SkyProperties> ci) {
		if (SkyEffects.tempHolder != null) {
			Optional<RegistryKey<DimensionType>> key = SkyEffects.tempHolder.getKey();
			if (key.isPresent()) {
				Optional<SkyEffects> sky = SkyEffects.SKY_EFFECTS.getOrEmpty(key.get().getValue());
				if (sky.isPresent()) {
					ci.setReturnValue(sky.get().getSkyProperties());
				}
			}
		}
		SkyEffects.tempHolder = null;
	}

}
