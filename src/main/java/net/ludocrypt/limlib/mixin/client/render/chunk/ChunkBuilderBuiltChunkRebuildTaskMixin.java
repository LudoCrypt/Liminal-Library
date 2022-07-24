package net.ludocrypt.limlib.mixin.client.render.chunk;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk.RebuildTask.RenderData;
import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

@Mixin(ChunkBuilder.BuiltChunk.RebuildTask.class)
public class ChunkBuilderBuiltChunkRebuildTaskMixin {

	@Shadow
	@Final
	BuiltChunk field_20839;

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask$RenderData;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkRendererRegion;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 0, shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$render(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage blockBufferBuilderStorage, CallbackInfoReturnable<RenderData> ci, RenderData renderData, int i, BlockPos blockPos, BlockPos blockPos2, ChunkOcclusionDataBuilder chunkOcclusionDataBuilder, ChunkRendererRegion chunkRendererRegion, MatrixStack matrixStack, Set<RenderLayer> set, Random random, BlockRenderManager blockRenderManager, Iterator<BlockPos> var15, BlockPos blockPos3) {
		BlockState blockState = chunkRendererRegion.getBlockState(blockPos3);
		if (!((BakedModelAccess) blockRenderManager.getModel(blockState)).getSubModels().isEmpty()) {
			((RenderDataAccess) (Object) renderData).getCustomQuadData().put(blockPos3.toImmutable(), blockState);
		}
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;run(Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$runChunkData(BlockBufferBuilderStorage buffers, CallbackInfoReturnable<CompletableFuture<?>> ci, Vec3d vec3d, float f, float g, float h, ChunkBuilder.BuiltChunk.RebuildTask.RenderData renderData, ChunkData chunkData) {
		((RenderDataAccess) field_20839).getCustomQuadData().clear();
		((RenderDataAccess) field_20839).getCustomQuadData().putAll(((RenderDataAccess) (Object) renderData).getCustomQuadData());
	}

}
