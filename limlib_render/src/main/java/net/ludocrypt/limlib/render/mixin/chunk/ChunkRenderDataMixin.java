package net.ludocrypt.limlib.render.mixin.chunk;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.ludocrypt.limlib.render.access.RenderedChunkDataAccess;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk.RebuildTask.RenderedChunkData;

@Mixin(RenderedChunkData.class)
public class ChunkRenderDataMixin implements RenderedChunkDataAccess {

	@Unique
	public final Map<SpecialModelRenderer, BufferBuilder.RenderedBuffer> specialModelBuffers = new Reference2ObjectArrayMap<>();

	@Override
	public Map<SpecialModelRenderer, RenderedBuffer> getSpecialModelBuffers() {
		return specialModelBuffers;
	}

}
