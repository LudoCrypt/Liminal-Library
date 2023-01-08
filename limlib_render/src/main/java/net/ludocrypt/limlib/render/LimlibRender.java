package net.ludocrypt.limlib.render;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.ludocrypt.limlib.render.skybox.EmptySkybox;
import net.ludocrypt.limlib.render.skybox.Skybox;
import net.ludocrypt.limlib.render.skybox.TexturedSkybox;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.ludocrypt.limlib.render.special.TexturedSpecialModelRenderer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.registry.Holder;
import net.minecraft.registry.HolderLookup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class LimlibRender implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("Limlib | Render");

	@ClientOnly
	public static final Map<SpecialModelRenderer, ShaderProgram> LOADED_SHADERS = Maps.newHashMap();

	@ClientOnly
	public static final List<Runnable> ITEM_RENDER_QUEUE = Lists.newArrayList();

	@ClientOnly
	public static final List<Runnable> HAND_RENDER_QUEUE = Lists.newArrayList();

	@Override
	public void onInitialize(ModContainer mod) {
		Registry.register(Skybox.SKYBOX_CODEC, new Identifier("limlib", "textured"), TexturedSkybox.CODEC);
		Registry.register(Skybox.SKYBOX_CODEC, new Identifier("limlib", "empty"), EmptySkybox.CODEC);

		TexturedSpecialModelRenderer.init();
	}

	public static <T> Optional<T> snatch(HolderLookup<T> lookup, RegistryKey<T> key) {
		Optional<Holder.Reference<T>> holderOptional = lookup.getHolder(key);

		if (holderOptional.isPresent()) {
			Holder.Reference<T> holder = holderOptional.get();
			try {
				T held = holder.value();
				return Optional.of(held);
			} catch (IllegalStateException e) {
				return Optional.empty();
			}
		}

		return Optional.empty();
	}

}
