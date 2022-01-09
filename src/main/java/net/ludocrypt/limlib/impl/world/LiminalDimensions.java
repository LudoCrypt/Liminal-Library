package net.ludocrypt.limlib.impl.world;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.world.LiminalWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;

public class LiminalDimensions {

	public static final SimpleRegistry<LiminalWorld> LIMINAL_WORLD_REGISTRY = FabricRegistryBuilder.createSimple(LiminalWorld.class, new Identifier("limlib", "liminal_world_registry")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static LiminalWorld register(RegistryKey<World> world, LiminalWorld limWorld) {
		return Registry.register(LIMINAL_WORLD_REGISTRY, world.getValue(), limWorld);
	}

}
