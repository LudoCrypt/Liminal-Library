package net.ludocrypt.limlib.mixin.client.render.chunk;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.ludocrypt.limlib.access.RenderDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk.RebuildTask.RenderData;
import net.minecraft.util.math.BlockPos;

@Mixin(RenderData.class)
public class RenderDataMixin implements RenderDataAccess {

	@Unique
	private final HashMap<BlockPos, BlockState> customQuadData = new HashMap<BlockPos, BlockState>();

	@Override
	public HashMap<BlockPos, BlockState> getCustomQuadData() {
		return customQuadData;
	}

}
