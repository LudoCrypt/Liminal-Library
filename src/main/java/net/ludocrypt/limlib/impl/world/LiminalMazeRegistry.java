package net.ludocrypt.limlib.impl.world;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.world.maze.MazeGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class LiminalMazeRegistry {

	// I can't parameterize this
	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends MazeGenerator>> MAZE_GENERATOR = (SimpleRegistry<Codec<? extends MazeGenerator>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "maze_generator_codec")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static Codec<? extends MazeGenerator> register(Identifier id, Codec<? extends MazeGenerator> maze) {
		return Registry.register(MAZE_GENERATOR, id, maze);
	}

}
