package net.ludocrypt.limlib.impl.world;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.render.LiminalShader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class LiminalShaderRegistry {

	// I can't parameterize this
	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends LiminalShader>> LIMINAL_SHADER = (SimpleRegistry<Codec<? extends LiminalShader>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "liminal_shader_codec")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static Codec<? extends LiminalShader> register(Identifier id, Codec<? extends LiminalShader> maze) {
		return Registry.register(LIMINAL_SHADER, id, maze);
	}

}
