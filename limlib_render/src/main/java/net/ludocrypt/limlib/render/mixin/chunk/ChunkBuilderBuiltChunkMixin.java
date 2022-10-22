package net.ludocrypt.limlib.render.mixin.chunk;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.ludocrypt.limlib.render.access.ChunkBuilderBuiltChunkAccessor;
import net.minecraft.client.render.chunk.ChunkBuilder;

@Mixin(ChunkBuilder.BuiltChunk.class)
public class ChunkBuilderBuiltChunkMixin implements ChunkBuilderBuiltChunkAccessor {

	@Shadow
	@Final
	ChunkBuilder field_20833;

	@Override
	public ChunkBuilder getInternalChunkBuilder() {
		return field_20833;
	}

}
