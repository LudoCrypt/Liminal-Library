package net.ludocrypt.limlib.render.mixin.registry;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;

import net.ludocrypt.limlib.render.skybox.Skybox;
import net.minecraft.registry.RegistryLoader;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

	@Shadow
	@Final
	@Mutable
	public static List<RegistryLoader.DecodingData<?>> WORLDGEN_REGISTRIES;

	static {
		List<RegistryLoader.DecodingData<?>> newRegistries = Lists.newArrayList();
		newRegistries.addAll(WORLDGEN_REGISTRIES);
		newRegistries.add(new RegistryLoader.DecodingData(Skybox.SKYBOX_KEY, Skybox.CODEC));
		WORLDGEN_REGISTRIES = newRegistries;
	}

}
