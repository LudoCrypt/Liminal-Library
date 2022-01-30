package net.ludocrypt.limlib.impl.render;

import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.api.render.LiminalShader;
import net.ludocrypt.limlib.api.world.LiminalWorld;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class LiminalShaderRegistry {

	public static final HashMap<RegistryKey<World>, LiminalShader> SHADER_REGISTRY = new HashMap<RegistryKey<World>, LiminalShader>();
	public static final LiminalShader DEFAULT = new LiminalShader.SimpleShader(new Identifier("shaders/post/empty_shader.json"));

	public static LiminalShader register(LiminalWorld world, LiminalShader shader) {
		return register(world.worldWorldRegistryKey, shader);
	}

	public static LiminalShader register(RegistryKey<World> world, LiminalShader shader) {
		return SHADER_REGISTRY.put(world, shader);
	}

	public static LiminalShader getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static LiminalShader getCurrent(RegistryKey<World> key) {
		return SHADER_REGISTRY.getOrDefault(key, DEFAULT);
	}

}
