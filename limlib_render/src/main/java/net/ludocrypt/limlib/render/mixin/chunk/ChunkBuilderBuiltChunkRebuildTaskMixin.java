package net.ludocrypt.limlib.render.mixin.chunk;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.render.access.BakedModelAccess;
import net.ludocrypt.limlib.render.access.ChunkBuilderAccess;
import net.ludocrypt.limlib.render.access.ChunkBuilderBuiltChunkAccessor;
import net.ludocrypt.limlib.render.access.RenderMapAccess;
import net.ludocrypt.limlib.render.util.SingleQuadBakedModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRenderRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;

@Mixin(ChunkBuilder.BuiltChunk.RebuildTask.class)
public class ChunkBuilderBuiltChunkRebuildTaskMixin {

	@Shadow(remap = false)
	@Final
	ChunkBuilder.BuiltChunk field_20839;

	@Unique
	private Map<BakedQuad, Pair<BufferBuilder, Identifier>> customBuffers;

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask$class_7435;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModelRenderer;enableBrightnessCache()V", shift = Shift.AFTER))
	private void limlib$render$before(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<ChunkBuilder.BuiltChunk.RebuildTask.class_7435> ci) {
		this.customBuffers = Maps.newHashMap();
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask$class_7435;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/random/RandomGenerator;)V", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$render(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<ChunkBuilder.BuiltChunk.RebuildTask.class_7435> ci, ChunkBuilder.BuiltChunk.RebuildTask.class_7435 lv, int i, BlockPos chunkMinPos, BlockPos chunkMaxPos, ChunkOcclusionDataBuilder chunkOcclusionDataBuilder, ChunkRenderRegion chunkRenderRegion, MatrixStack matrixStack, Set<RenderLayer> set, RandomGenerator randomGenerator, BlockRenderManager blockRenderManager, Iterator<BlockPos> chunkPosIterator, BlockPos pos, BlockState blockState) {
		List<Pair<Identifier, BakedModel>> models = ((BakedModelAccess) blockRenderManager.getModel(blockState)).getModels(blockState);
		if (!models.isEmpty()) {
			for (Pair<Identifier, BakedModel> pair : models) {
				Identifier id = pair.getFirst();
				BakedModel model = pair.getSecond();

				long modelSeed = blockState.getRenderingSeed(pos);
				Set<BakedQuad> bakedQuads = this.limlib$getQuads(model, blockState, pos, chunkRenderRegion, randomGenerator, modelSeed);

				for (BakedQuad quad : bakedQuads) {
					if (!customBuffers.containsKey(quad)) {
						customBuffers.put(quad, Pair.of(this.limlib$makeBufferBuilder(), id));
					}

					blockRenderManager.getModelRenderer().render(chunkRenderRegion, new SingleQuadBakedModel(model, quad), blockState, pos, matrixStack, customBuffers.get(quad).getFirst(), false, randomGenerator, modelSeed, OverlayTexture.DEFAULT_UV);
				}
			}
		}
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask$class_7435;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockModelRenderer;disableBrightnessCache()V", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$render$after(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage buffers, CallbackInfoReturnable<ChunkBuilder.BuiltChunk.RebuildTask.class_7435> ci, ChunkBuilder.BuiltChunk.RebuildTask.class_7435 lv) {
		this.customBuffers.forEach((quad, renderPair) -> {
			RenderedBuffer renderedBuffer = renderPair.getFirst().endOrDiscard();
			if (renderedBuffer != null) {
				((RenderMapAccess) (Object) lv).getRenderMap().put(quad, Pair.of(renderedBuffer, renderPair.getSecond()));
			}
		});

		this.customBuffers = null;
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;run(Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$run(BlockBufferBuilderStorage buffers, CallbackInfoReturnable<CompletableFuture<ChunkBuilder.Result>> ci, Vec3d vec3d, float f, float g, float h, ChunkBuilder.BuiltChunk.RebuildTask.class_7435 lv) {
		((RenderMapAccess) (Object) lv).getRenderMap().values().stream().map(Pair::getFirst).forEach(BufferBuilder.RenderedBuffer::release);
	}

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;run(Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$run(BlockBufferBuilderStorage buffers, CallbackInfoReturnable<CompletableFuture<ChunkBuilder.Result>> ci, Vec3d vec3d, float f, float g, float h, ChunkBuilder.BuiltChunk.RebuildTask.class_7435 lv, ChunkBuilder.ChunkData chunkData, List<CompletableFuture<Void>> list) {
		Queue<Runnable> queue = ((ChunkBuilderAccess) ((ChunkBuilderBuiltChunkAccessor) field_20839).getInternalChunkBuilder()).getRenderUploadQueue();

		((RenderMapAccess) (Object) lv).getRenderMap().forEach((quad, renderPair) -> {
			list.add(CompletableFuture.runAsync(() -> {
				Map<BakedQuad, Pair<VertexBuffer, Identifier>> renderBufferMap = ((RenderMapAccess) chunkData).getShaderBuffers();

				if (!renderBufferMap.containsKey(quad)) {
					renderBufferMap.put(quad, Pair.of(new VertexBuffer(), renderPair.getSecond()));
				}

				((RenderMapAccess) chunkData).getShaderBuffers().put(quad, renderBufferMap.get(quad));

				limlib$scheduleUpload(renderPair.getFirst(), ((RenderMapAccess) chunkData).getShaderBuffers().get(quad).getFirst());
			}, queue::add));
		});

		((RenderMapAccess) chunkData).getRenderMap().putAll(((RenderMapAccess) (Object) lv).getRenderMap());

	}

	@Inject(method = "method_23619(Lnet/minecraft/client/render/chunk/ChunkBuilder$ChunkData;Ljava/util/List;Ljava/lang/Throwable;)Lnet/minecraft/client/render/chunk/ChunkBuilder$Result;", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/atomic/AtomicReference;set(Ljava/lang/Object;)V", shift = Shift.BEFORE, remap = false))
	private void limlib$releaseVertexBuffers(ChunkBuilder.ChunkData data, List<CompletableFuture<Void>> list, Throwable throwable, CallbackInfoReturnable<ChunkBuilder.Result> ci) {
		((RenderMapAccess) this.field_20839.data.get()).getShaderBuffers().values().stream().map(Pair::getFirst).forEach(VertexBuffer::close);
	}

	@Unique
	private void limlib$scheduleUpload(RenderedBuffer buffer, VertexBuffer glBuffer) {
		if (!glBuffer.invalid()) {
			glBuffer.bind();
			glBuffer.upload(buffer);
			VertexBuffer.unbind();
		}
	}

	@Unique
	private BufferBuilder limlib$makeBufferBuilder() {
		BufferBuilder builder = new BufferBuilder(2097152);
		builder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
		return builder;
	}

	@Unique
	private Set<BakedQuad> limlib$getQuads(BakedModel model, BlockState state, BlockPos pos, ChunkRenderRegion world, RandomGenerator randomGenerator, long seed) {
		Set<BakedQuad> bakedQuads = Sets.newHashSet();

		for (Direction dir : Direction.values()) {
			randomGenerator.setSeed(seed);
			if (Block.shouldDrawSide(state, world, pos, dir, pos.offset(dir))) {
				bakedQuads.addAll(model.getQuads(state, dir, randomGenerator));
			}
		}

		randomGenerator.setSeed(seed);
		bakedQuads.addAll(model.getQuads(state, null, randomGenerator));

		return bakedQuads;
	}

}
