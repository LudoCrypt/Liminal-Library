package net.ludocrypt.limlib.mixin;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Maps;

import net.ludocrypt.limlib.access.RenderDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;

@Mixin(ChunkBuilder.BuiltChunk.class)
public class ChunkBuilderBuiltChunkMixin implements RenderDataAccess {

	@Unique
	private HashMap<BlockPos, BlockState> quadData = Maps.newHashMap();

	@Override
	public HashMap<BlockPos, BlockState> getCustomQuadData() {
		return quadData;
	}

}
