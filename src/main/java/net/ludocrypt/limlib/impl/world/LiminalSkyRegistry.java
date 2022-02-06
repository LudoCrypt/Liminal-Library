package net.ludocrypt.limlib.impl.world;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.render.SkyHook;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class LiminalSkyRegistry {

	// I can't parameterize this
	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends SkyHook>> LIMINAL_SKY = (SimpleRegistry<Codec<? extends SkyHook>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "liminal_sky_codec")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static Codec<? extends SkyHook> register(Identifier id, Codec<? extends SkyHook> maze) {
		return Registry.register(LIMINAL_SKY, id, maze);
	}

}
