package net.ludocrypt.limlib.impl.world;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.world.LiminalWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class LiminalDimensions {

	public static final SimpleRegistry<LiminalWorld> LIMINAL_WORLD_REGISTRY = FabricRegistryBuilder.createSimple(LiminalWorld.class, new Identifier("limlib", "liminal_world_registry")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static LiminalWorld register(Identifier id, LiminalWorld limWorld) {
		return Registry.register(LIMINAL_WORLD_REGISTRY, id, limWorld);
	}

}
