package net.ludocrypt.limlib.render.mixin.chunk;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.render.access.RenderMapAccess;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

@Mixin(ChunkBuilder.ChunkData.class)
public class ChunkBuilderChunkDataMixin implements RenderMapAccess {

	@Unique
	public Map<BakedQuad, Pair<BufferBuilder.RenderedBuffer, Identifier>> renderMap;

	@Unique
	private Map<BakedQuad, Pair<VertexBuffer, Identifier>> shaderBuffers;

	@Override
	public Map<BakedQuad, Pair<BufferBuilder.RenderedBuffer, Identifier>> getRenderMap() {
		if (renderMap == null) {
			renderMap = Maps.newHashMap();
		}
		return renderMap;
	}

	@Override
	public Map<BakedQuad, Pair<VertexBuffer, Identifier>> getShaderBuffers() {
		if (shaderBuffers == null) {
			shaderBuffers = Maps.newHashMap();
		}
		return shaderBuffers;
	}
}
