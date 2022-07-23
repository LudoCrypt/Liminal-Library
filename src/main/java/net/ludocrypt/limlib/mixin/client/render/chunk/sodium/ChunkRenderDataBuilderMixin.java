package net.ludocrypt.limlib.mixin.client.render.chunk.sodium;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderData;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

@Pseudo
@Mixin(ChunkRenderData.Builder.class)
public class ChunkRenderDataBuilderMixin implements RenderDataAccess {

	@Unique
	private HashMap<BlockPos, BlockState> quadData = Maps.newHashMap();

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/data/ChunkRenderData$Builder;build()Lme/jellysquid/mods/sodium/client/render/chunk/data/ChunkRenderData;", at = @At("RETURN"), remap = false)
	private void limlib$build(CallbackInfoReturnable<ChunkRenderData> ci) {
		((RenderDataAccess) ci.getReturnValue()).getCustomQuadData().putAll(quadData);
	}

	@Override
	public HashMap<BlockPos, BlockState> getCustomQuadData() {
		return quadData;
	}
}
