package net.ludocrypt.limlib.mixin.common.world.registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.registry.BuiltinRegistries;

@Mixin(BuiltinRegistries.class)
public class BuiltinRegistriesMixin {

	@Inject(method = "<clinit>", at = @At("HEAD"))
	private static void limlib$clinit(CallbackInfo ci) {
		FabricLoader.getInstance().getEntrypoints("limlib", Runnable.class).forEach(Runnable::run);
	}

}
