package net.ludocrypt.limlib.render.access;

import java.util.Map;

import com.mojang.blaze3d.vertex.VertexBuffer;

import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;

public interface BuiltChunkAccess {

	public VertexBuffer getBuffer(SpecialModelRenderer modelRenderer);

	public Map<SpecialModelRenderer, VertexBuffer> getSpecialModelBuffers();

	public ChunkBuilder getSuperChunkBuilder();

}
