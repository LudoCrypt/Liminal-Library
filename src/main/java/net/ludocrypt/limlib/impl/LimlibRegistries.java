package net.ludocrypt.limlib.impl;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.LiminalWorld;
import net.ludocrypt.limlib.api.render.LiminalBaseEffects;
import net.ludocrypt.limlib.api.render.LiminalShaderApplier;
import net.ludocrypt.limlib.api.render.LiminalSkyRenderer;
import net.ludocrypt.limlib.api.world.maze.deprecated.MazeGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

@SuppressWarnings({ "unchecked", "deprecation" })
public class LimlibRegistries {

	public static final SimpleRegistry<LiminalWorld> LIMINAL_WORLD = FabricRegistryBuilder.createSimple(LiminalWorld.class, new Identifier("limlib", "limlib_world")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static final SimpleRegistry<Codec<? extends LiminalSkyRenderer>> LIMINAL_SKY_RENDERER = (SimpleRegistry<Codec<? extends LiminalSkyRenderer>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_sky_renderer")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SimpleRegistry<Codec<? extends LiminalShaderApplier>> LIMINAL_SHADER_APPLIER = (SimpleRegistry<Codec<? extends LiminalShaderApplier>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_shader_applier")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SimpleRegistry<Codec<? extends LiminalBaseEffects>> LIMINAL_BASE_EFFECTS = (SimpleRegistry<Codec<? extends LiminalBaseEffects>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_base_effects")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static final SimpleRegistry<Codec<? extends MazeGenerator>> LIMINAL_MAZE_GENERATOR = (SimpleRegistry<Codec<? extends MazeGenerator>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_maze_generator")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

}
