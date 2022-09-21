package net.ludocrypt.limlib.render.access;

import java.util.Map;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

public interface RenderMapAccess {

	public Map<BakedQuad, Pair<BufferBuilder.RenderedBuffer, Identifier>> getRenderMap();

	public Map<BakedQuad, Pair<VertexBuffer, Identifier>> getShaderBuffers();

}
