package net.ludocrypt.limlib.render.mixin.chunk;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.ludocrypt.limlib.render.access.RenderMapAccess;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

@Mixin(ChunkBuilder.BuiltChunk.RebuildTask.class_7435.class)
public class ChunkBuilderBuiltChunkRebuildTaskClass_7435Mixin implements RenderMapAccess {

	@Unique
	public final Map<BakedQuad, Pair<BufferBuilder.RenderedBuffer, Identifier>> renderMap = new Reference2ObjectArrayMap<>();

	@Unique
	private final Map<BakedQuad, Pair<VertexBuffer, Identifier>> shaderBuffers = Maps.newHashMap();

	@Override
	public Map<BakedQuad, Pair<BufferBuilder.RenderedBuffer, Identifier>> getRenderMap() {
		return renderMap;
	}

	@Override
	public Map<BakedQuad, Pair<VertexBuffer, Identifier>> getShaderBuffers() {
		return shaderBuffers;
	}

}
