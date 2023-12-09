package net.ludocrypt.limlib.impl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.impl.Limlib;
import net.minecraft.Bootstrap;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

	@Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;registerDefaults()V", shift = Shift.AFTER))
	private static void limlib$initialize(CallbackInfo ci) {
		Limlib.onInitialize();
	}

}
