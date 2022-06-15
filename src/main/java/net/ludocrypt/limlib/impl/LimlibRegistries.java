package net.ludocrypt.limlib.impl;

import com.mojang.serialization.Codec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.LiminalWorld;
import net.ludocrypt.limlib.api.render.LiminalBaseEffects;
import net.ludocrypt.limlib.api.render.LiminalCoreShader;
import net.ludocrypt.limlib.api.render.LiminalQuadRenderer;
import net.ludocrypt.limlib.api.render.LiminalShaderApplier;
import net.ludocrypt.limlib.api.render.LiminalSkyRenderer;
import net.ludocrypt.limlib.api.world.maze.MazeComponent;
import net.ludocrypt.limlib.api.world.maze.MazeGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@SuppressWarnings("unchecked")
public class LimlibRegistries {

	public static final SimpleRegistry<LiminalWorld> LIMINAL_WORLD = FabricRegistryBuilder.createSimple(LiminalWorld.class, new Identifier("limlib", "limlib_world")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static final SimpleRegistry<Codec<? extends LiminalSkyRenderer>> LIMINAL_SKY_RENDERER = (SimpleRegistry<Codec<? extends LiminalSkyRenderer>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_sky_renderer")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SimpleRegistry<Codec<? extends LiminalShaderApplier>> LIMINAL_SHADER_APPLIER = (SimpleRegistry<Codec<? extends LiminalShaderApplier>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_shader_applier")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final SimpleRegistry<Codec<? extends LiminalBaseEffects>> LIMINAL_BASE_EFFECTS = (SimpleRegistry<Codec<? extends LiminalBaseEffects>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_base_effects")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	@Environment(EnvType.CLIENT)
	public static final SimpleRegistry<LiminalQuadRenderer> LIMINAL_QUAD_RENDERER = FabricRegistryBuilder.createSimple(LiminalQuadRenderer.class, new Identifier("limlib", "limlib_quad_renderer")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	@Environment(EnvType.CLIENT)
	public static final SimpleRegistry<LiminalCoreShader> LIMINAL_CORE_SHADER = FabricRegistryBuilder.createSimple(LiminalCoreShader.class, new Identifier("limlib", "limlib_core_shader")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static final SimpleRegistry<Codec<? extends MazeGenerator<? extends ChunkGenerator, ? extends MazeComponent>>> LIMINAL_MAZE_GENERATOR = (SimpleRegistry<Codec<? extends MazeGenerator<? extends ChunkGenerator, ? extends MazeComponent>>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_maze_generator")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

}
