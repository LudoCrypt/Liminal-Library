package net.ludocrypt.limlib.mixin.client.render.chunk;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.ChunkInfo.class)
public interface ChunkInfoAccessor {

	@Accessor
	ChunkBuilder.BuiltChunk getChunk();

}
