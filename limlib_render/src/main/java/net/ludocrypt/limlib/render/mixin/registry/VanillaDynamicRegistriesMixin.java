package net.ludocrypt.limlib.render.mixin.registry;

import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.render.skybox.Skybox;
import net.ludocrypt.limlib.render.skybox.SkyboxBootstrap;
import net.minecraft.registry.RegistrySetBuilder;
import net.minecraft.registry.VanillaDynamicRegistries;

@Mixin(VanillaDynamicRegistries.class)
public class VanillaDynamicRegistriesMixin {

	@Shadow
	@Final
	private static RegistrySetBuilder BUILDER;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void limlib$clinit(CallbackInfo ci) {
		BUILDER.add(Skybox.SKYBOX_KEY, (context) -> QuiltLoader.getEntrypoints(SkyboxBootstrap.ENTRYPOINT_KEY, SkyboxBootstrap.class).forEach((bootstrap) -> bootstrap.register(context)));
	}

}
