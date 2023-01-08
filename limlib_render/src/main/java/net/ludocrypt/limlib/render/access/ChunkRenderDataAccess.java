package net.ludocrypt.limlib.render.access;

import java.util.Map;

import com.mojang.blaze3d.vertex.BufferBuilder;

import net.ludocrypt.limlib.render.special.SpecialModelRenderer;

public interface ChunkRenderDataAccess {

	public Map<SpecialModelRenderer, BufferBuilder.RenderedBuffer> getSpecialModelBuffers();

}
