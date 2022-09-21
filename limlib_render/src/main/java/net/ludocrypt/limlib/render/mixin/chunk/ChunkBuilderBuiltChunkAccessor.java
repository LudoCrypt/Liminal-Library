package net.ludocrypt.limlib.render.mixin.chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.chunk.ChunkBuilder;

@Mixin(ChunkBuilder.BuiltChunk.class)
public interface ChunkBuilderBuiltChunkAccessor {

	@Accessor(value = "field_20833", remap = false)
	public ChunkBuilder getInternalChunkBuilder();

}
