package net.ludocrypt.limlib.mixin.client.render.chunk.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildResult;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderData;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import me.jellysquid.mods.sodium.client.util.task.CancellationSource;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.ludocrypt.limlib.access.ModelAccess;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos.Mutable;

@Pseudo
@Mixin(ChunkRenderRebuildTask.class)
public class ChunkRenderRebuildTaskMixin {

	@Group(name = "performBuild", min = 1)
	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/tasks/ChunkRenderRebuildTask;performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos$Mutable;set(III)Lnet/minecraft/util/math/BlockPos$Mutable;", ordinal = 1, shift = Shift.AFTER, remap = false), locals = LocalCapture.CAPTURE_FAILHARD, remap = false, require = 0)
	private void limlib$performBuild$mapped(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> ci, ChunkRenderData.Builder renderData, ChunkOcclusionDataBuilder occluder, ChunkRenderBounds.Builder bounds, ChunkBuildBuffers buffers, ChunkRenderCacheLocal cache, WorldSlice slice, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Mutable blockPos, Mutable offset) {
		BlockState blockState = slice.getBlockState(blockPos);
		if (((ModelAccess) cache.getBlockModels().getModel(blockState)).getLiminalQuadRenderer().isPresent()) {
			((RenderDataAccess) (Object) renderData).getCustomQuadData().put(blockPos.toImmutable(), blockState);
		}
	}

	@Group(name = "performBuild", min = 1)
	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/tasks/ChunkRenderRebuildTask;performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2338$class_2339;method_10103(III)Lnet/minecraft/class_2338$class_2339;", ordinal = 1, shift = Shift.AFTER, remap = false), locals = LocalCapture.CAPTURE_FAILHARD, remap = false, require = 0)
	private void limlib$performBuild$unmapped(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> ci, ChunkRenderData.Builder renderData, ChunkOcclusionDataBuilder occluder, ChunkRenderBounds.Builder bounds, ChunkBuildBuffers buffers, ChunkRenderCacheLocal cache, WorldSlice slice, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Mutable blockPos, Mutable offset) {
		BlockState blockState = slice.getBlockState(blockPos);
		if (((ModelAccess) cache.getBlockModels().getModel(blockState)).getLiminalQuadRenderer().isPresent()) {
			((RenderDataAccess) (Object) renderData).getCustomQuadData().put(blockPos.toImmutable(), blockState);
		}
	}

}
