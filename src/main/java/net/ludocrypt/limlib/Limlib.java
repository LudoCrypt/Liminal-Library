package net.ludocrypt.limlib;

import net.fabricmc.api.ModInitializer;
import net.ludocrypt.limlib.api.render.LiminalBaseEffects;
import net.ludocrypt.limlib.api.render.LiminalShaderApplier;
import net.ludocrypt.limlib.api.render.LiminalSkyRenderer;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Limlib implements ModInitializer {

	@Override
	public void onInitialize() {
		Registry.register(LimlibRegistries.LIMINAL_SHADER_APPLIER, new Identifier("limlib", "simple_shader"), LiminalShaderApplier.SimpleShader.CODEC);
		Registry.register(LimlibRegistries.LIMINAL_SKY_RENDERER, new Identifier("limlib", "regular_sky"), LiminalSkyRenderer.RegularSky.CODEC);
		Registry.register(LimlibRegistries.LIMINAL_SKY_RENDERER, new Identifier("limlib", "skybox_sky"), LiminalSkyRenderer.SkyboxSky.CODEC);
		Registry.register(LimlibRegistries.LIMINAL_BASE_EFFECTS, new Identifier("limlib", "simple_base_effects"), LiminalBaseEffects.SimpleBaseEffects.CODEC);
	}

}
