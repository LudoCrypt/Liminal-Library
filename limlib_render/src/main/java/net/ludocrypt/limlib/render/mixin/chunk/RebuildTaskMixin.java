package net.ludocrypt.limlib.render.mixin.chunk;

import java.util.Iterator;
import java.util.List;
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

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.render.access.BakedModelAccess;
import net.ludocrypt.limlib.render.access.BlockBufferBuilderStorageAccess;
import net.ludocrypt.limlib.render.access.BuiltChunkAccess;
import net.ludocrypt.limlib.render.access.ChunkRenderDataAccess;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk.RebuildTask;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk.RebuildTask.ChunkRenderData;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRenderRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;

@Mixin(RebuildTask.class)
public class RebuildTaskMixin {

	@Shadow(remap = false)
	@Final
	ChunkBuilder.BuiltChunk f_yzjdcnsj;

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask$ChunkRenderData;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/random/RandomGenerator;)V", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$render(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<ChunkRenderData> ci, ChunkRenderData chunkData, int i, BlockPos blockPos, BlockPos blockPos2, ChunkOcclusionDataBuilder chunkOcclusionDataBuilder, ChunkRenderRegion chunkRenderRegion, MatrixStack matrixStack, Set<RenderLayer> set, RandomGenerator randomGenerator, BlockRenderManager blockRenderManager, Iterator<BlockPos> var15, BlockPos pos, BlockState blockState, BlockState blockState2, FluidState fluidState, RenderLayer renderLayer, BufferBuilder bufferBuilder) {
		List<Pair<SpecialModelRenderer, BakedModel>> models = ((BakedModelAccess) blockRenderManager.getModel(blockState)).getModels(blockState);
		if (!models.isEmpty()) {
			for (Pair<SpecialModelRenderer, BakedModel> pair : models) {
				SpecialModelRenderer modelRenderer = pair.getFirst();
				BakedModel model = pair.getSecond();

				long modelSeed = blockState.getRenderingSeed(pos);

				BufferBuilder buffer = ((BlockBufferBuilderStorageAccess) buffers).get(modelRenderer);
				if (!buffer.isBuilding()) {
					buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
				}

				blockRenderManager.getModelRenderer().render(chunkRenderRegion, model, blockState, pos, matrixStack, buffer, true, randomGenerator, modelSeed, OverlayTexture.DEFAULT_UV);
			}
		}
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask$ChunkRenderData;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModelRenderer;disableBrightnessCache()V", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$render(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<ChunkRenderData> ci, ChunkRenderData chunkData) {
		for (SpecialModelRenderer modelRenderer : ((BlockBufferBuilderStorageAccess) buffers).getSpecialModelBuffers().keySet()) {
			if (!((BlockBufferBuilderStorageAccess) buffers).get(modelRenderer).isBuilding()) {
				((BlockBufferBuilderStorageAccess) buffers).get(modelRenderer).begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
			}

			BufferBuilder.RenderedBuffer renderedBuffer = ((BlockBufferBuilderStorageAccess) buffers).get(modelRenderer).end();
			if (renderedBuffer != null) {
				((ChunkRenderDataAccess) (Object) chunkData).getSpecialModelBuffers().put(modelRenderer, renderedBuffer);
			}
		}
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;run(Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$run(BlockBufferBuilderStorage buffers, CallbackInfoReturnable<CompletableFuture<ChunkBuilder.Result>> ci, Vec3d vec3d, float f, float g, float h, ChunkRenderData chunkRenderData) {
		((ChunkRenderDataAccess) (Object) chunkRenderData).getSpecialModelBuffers().values().forEach(BufferBuilder.RenderedBuffer::release);
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;run(Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$run(BlockBufferBuilderStorage buffers, CallbackInfoReturnable<CompletableFuture<ChunkBuilder.Result>> ci, Vec3d vec3d, float f, float g, float h, ChunkRenderData chunkRenderData, ChunkBuilder.ChunkData chunkData, List<CompletableFuture<Void>> list) {
		((ChunkRenderDataAccess) (Object) chunkRenderData).getSpecialModelBuffers().forEach((modelRenderer, renderedBuffer) -> list.add(((BuiltChunkAccess) f_yzjdcnsj).getSuperChunkBuilder().scheduleUpload(renderedBuffer, ((BuiltChunkAccess) f_yzjdcnsj).getBuffer(modelRenderer))));
	}
}
