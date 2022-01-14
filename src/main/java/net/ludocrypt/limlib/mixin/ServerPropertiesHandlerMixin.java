package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.api.world.LevelStorageHacks;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;

@Mixin(ServerPropertiesHandler.class)
public class ServerPropertiesHandlerMixin {

	@Inject(method = "getGeneratorOptions", at = @At("HEAD"))
	private void limlib$getGeneratorOptions(DynamicRegistryManager registryManager, CallbackInfoReturnable<GeneratorOptions> ci) {
		if (LevelStorageHacks.earlyDynamicRegistryManager == null) {
			LevelStorageHacks.earlyDynamicRegistryManager = (DynamicRegistryManager.Impl) registryManager;
		}
	}

}
