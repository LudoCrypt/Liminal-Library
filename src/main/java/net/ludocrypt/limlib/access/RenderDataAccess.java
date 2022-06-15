package net.ludocrypt.limlib.access;

import java.util.HashMap;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public interface RenderDataAccess {

	public HashMap<BlockPos, BlockState> getCustomQuadData();

}
