package net.ludocrypt.limlib.mixin.client.render.chunk.sodium;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;

import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements RenderDataAccess, WorldRendererExtended {

	@Override
	public HashMap<BlockPos, BlockState> getCustomQuadData() {
		return ((RenderDataAccess) ((SodiumWorldRendererAccessor) this.getSodiumWorldRenderer()).getRenderSectionManager()).getCustomQuadData();
	}

}
