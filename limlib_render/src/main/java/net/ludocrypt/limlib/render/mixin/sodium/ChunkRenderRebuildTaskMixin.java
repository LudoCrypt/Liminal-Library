package net.ludocrypt.limlib.render.mixin.sodium;

import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildResult;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderData;
import me.jellysquid.mods.sodium.client.render.chunk.format.ChunkModelVertexFormats;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPassManager;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import me.jellysquid.mods.sodium.client.util.task.CancellationSource;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.ludocrypt.limlib.render.access.BakedModelAccess;
import net.ludocrypt.limlib.render.access.ChunkBuildContextAccess;
import net.ludocrypt.limlib.render.util.SingleQuadBakedModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockRenderView;

@Mixin(ChunkRenderRebuildTask.class)
public class ChunkRenderRebuildTaskMixin {

	@Shadow
	@Final
	private RenderSection render;

	@Unique
	private MinecraftClient client = MinecraftClient.getInstance();

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/tasks/ChunkRenderRebuildTask;performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;", at = @At("HEAD"))
	public void limlib$performBuild$head(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> ci) {

	}

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/tasks/ChunkRenderRebuildTask;performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/pipeline/BlockRenderer;renderModel(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/model/BakedModel;Lme/jellysquid/mods/sodium/client/render/chunk/compile/buffers/ChunkModelBuilder;ZJ)Z", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	public void limlib$performBuild(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> ci, ChunkRenderData.Builder renderData, ChunkOcclusionDataBuilder occluder, ChunkRenderBounds.Builder bounds, ChunkBuildBuffers buffers, ChunkRenderCacheLocal cache, WorldSlice slice, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPos.Mutable blockPos, BlockPos.Mutable offset) {
		MatrixStack matrixStack = new MatrixStack();
		matrixStack.push();
		matrixStack.translate((double) (blockPos.toImmutable().getX() & 15), (double) (blockPos.toImmutable().getY() & 15), (double) (blockPos.toImmutable().getZ() & 15));

		BlockState state = slice.getBlockState(blockPos.toImmutable());
		BakedModel baseModel = cache.getBlockModels().getModel(state);
		long modelSeed = state.getRenderingSeed(blockPos);

		List<Pair<Identifier, BakedModel>> models = ((BakedModelAccess) baseModel).getModels(state);
		if (!models.isEmpty()) {
			for (Pair<Identifier, BakedModel> pair : models) {
				Identifier id = pair.getFirst();
				BakedModel model = pair.getSecond();

				Set<BakedQuad> bakedQuads = this.limlib$getQuads(model, state, blockPos.toImmutable(), slice, RandomGenerator.createLegacy(), modelSeed);

				for (BakedQuad quad : bakedQuads) {
					if (!((ChunkBuildContextAccess) buildContext).getBuildBuffersMap().containsKey(quad)) {
						((ChunkBuildContextAccess) buildContext).getBuildBuffersMap().put(quad, Pair.of(this.limlib$makeBuildBuffer(), id));
					}

					cache.getBlockRenderer().renderModel(slice, state, blockPos.toImmutable(), offset.toImmutable(), new SingleQuadBakedModel(model, quad), ((ChunkBuildContextAccess) buildContext).getBuildBuffersMap().get(quad).getFirst().get(RenderLayer.getTranslucent()), false, modelSeed);
				}
			}
		}
		matrixStack.pop();
	}

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/tasks/ChunkRenderRebuildTask;performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;", at = @At("TAIL"))
	public void limlib$performBuild$tail(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> ci) {

	}

	@Unique
	private ChunkBuildBuffers limlib$makeBuildBuffer() {
		ChunkBuildBuffers buildBuffer = new ChunkBuildBuffers(ChunkModelVertexFormats.DEFAULT, BlockRenderPassManager.createDefaultMappings());
		buildBuffer.init(new ChunkRenderData.Builder(), this.render.getChunkId());
		return buildBuffer;
	}

	@Unique
	private Set<BakedQuad> limlib$getQuads(BakedModel model, BlockState state, BlockPos pos, BlockRenderView world, RandomGenerator randomGenerator, long seed) {
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
