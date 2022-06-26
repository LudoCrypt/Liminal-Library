package net.ludocrypt.limlib.impl;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.render.LiminalCoreShader;
import net.ludocrypt.limlib.api.render.LiminalQuadRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

public class LimlibRendering {

	public static final SimpleRegistry<LiminalQuadRenderer> LIMINAL_QUAD_RENDERER = FabricRegistryBuilder.createSimple(LiminalQuadRenderer.class, new Identifier("limlib", "limlib_quad_renderer")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static final SimpleRegistry<LiminalCoreShader> LIMINAL_CORE_SHADER = FabricRegistryBuilder.createSimple(LiminalCoreShader.class, new Identifier("limlib", "limlib_core_shader")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

}
