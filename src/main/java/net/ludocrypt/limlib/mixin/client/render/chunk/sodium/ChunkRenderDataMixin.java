package net.ludocrypt.limlib.mixin.client.render.chunk.sodium;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Maps;

import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderData;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

@Pseudo
@Mixin(ChunkRenderData.class)
public class ChunkRenderDataMixin implements RenderDataAccess {

	@Unique
	private HashMap<BlockPos, BlockState> quadData = Maps.newHashMap();

	@Override
	public HashMap<BlockPos, BlockState> getCustomQuadData() {
		return quadData;
	}
}
