package net.ludocrypt.limlib.render;

import java.util.List;
import java.util.Map;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.render.skybox.EmptySkybox;
import net.ludocrypt.limlib.render.skybox.Skybox;
import net.ludocrypt.limlib.render.skybox.TexturedSkybox;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.ludocrypt.limlib.render.special.TexturedSpecialModelRenderer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LimlibRender implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("Limlib | Render");

	@Environment(EnvType.CLIENT)
	public static final Map<Identifier, ShaderProgram> LOADED_SHADERS = Maps.newHashMap();

	@Environment(EnvType.CLIENT)
	public static final List<Runnable> ITEM_RENDER_QUEUE = Lists.newArrayList();

	@Environment(EnvType.CLIENT)
	public static final List<Runnable> HAND_RENDER_QUEUE = Lists.newArrayList();

	@Environment(EnvType.CLIENT)
	public static final Map<SpecialModelRenderer, Identifier> SPECIAL_MODEL_RENDERERS = Maps.newHashMap();

	@Override
	public void onInitialize(ModContainer mod) {
		Registry.register(Skybox.SKYBOX_CODEC, new Identifier("limlib", "textured"), TexturedSkybox.CODEC);
		Registry.register(Skybox.SKYBOX_CODEC, new Identifier("limlib", "empty"), EmptySkybox.CODEC);

		Registry.register(SpecialModelRenderer.SPECIAL_MODEL_RENDERER_CODEC, new Identifier("limlib", "textured"), TexturedSpecialModelRenderer.CODEC);
	}

}
