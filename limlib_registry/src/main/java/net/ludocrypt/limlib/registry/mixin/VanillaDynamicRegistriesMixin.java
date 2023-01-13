package net.ludocrypt.limlib.registry.mixin;

import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.registry.registration.DimensionBootstrap;
import net.minecraft.registry.VanillaDynamicRegistries;

@Mixin(VanillaDynamicRegistries.class)
public class VanillaDynamicRegistriesMixin {

	@Inject(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/registry/RegistrySetBuilder", shift = Shift.BEFORE, ordinal = 0))
	private static void limlib$clinit(CallbackInfo ci) {
		QuiltLoader.getEntrypoints(DimensionBootstrap.ENTRYPOINT_KEY, DimensionBootstrap.class).forEach(DimensionBootstrap::register);
	}

}
