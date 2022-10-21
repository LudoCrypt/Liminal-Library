package net.ludocrypt.limlib.render.mixin.sodium;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;

import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildResult;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderBounds;
import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderData;
import me.jellysquid.mods.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import me.jellysquid.mods.sodium.client.render.occlusion.BlockOcclusionCache;
import me.jellysquid.mods.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import me.jellysquid.mods.sodium.client.util.task.CancellationSource;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.ludocrypt.limlib.render.access.BakedModelAccess;
import net.ludocrypt.limlib.render.access.sodium.ChunkBuildResultAccess;
import net.ludocrypt.limlib.render.util.SingleQuadBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
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
	private Map<BakedQuad, Pair<BufferBuilder, Identifier>> chunkBufferBuilderMap = Maps.newHashMap();

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/tasks/ChunkRenderRebuildTask;performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getFluidState()Lnet/minecraft/fluid/FluidState;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$performBuild(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> ci, ChunkRenderData.Builder renderData, ChunkOcclusionDataBuilder occluder, ChunkRenderBounds.Builder bounds, ChunkBuildBuffers buffers, ChunkRenderCacheLocal cache, WorldSlice slice, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPos.Mutable blockPos, BlockPos.Mutable offset, int y, int z, int x, BlockState blockState) {
		MinecraftClient client = MinecraftClient.getInstance();
		BlockPos pos = blockPos.toImmutable();
		List<Pair<Identifier, BakedModel>> models = ((BakedModelAccess) cache.getBlockModels().getModel(blockState)).getModels(blockState);
		if (!models.isEmpty()) {
			for (Pair<Identifier, BakedModel> pair : models) {
				Identifier id = pair.getFirst();
				BakedModel model = pair.getSecond();

				long modelSeed = blockState.getRenderingSeed(pos);
				Set<BakedQuad> bakedQuads = this.limlib$getQuads(model, blockState, pos, slice, ((BlockRendererAccessor) cache.getBlockRenderer()).getOcclusionCache(), RandomGenerator.createLegacy(), modelSeed);

				for (BakedQuad quad : bakedQuads) {

					if (!this.chunkBufferBuilderMap.containsKey(quad)) {
						this.chunkBufferBuilderMap.put(quad, Pair.of(limlib$makeBufferBuilder(), id));
					}

					MatrixStack stack = new MatrixStack();
					stack.translate(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
					client.getBlockRenderManager().getModelRenderer().render(slice, new SingleQuadBakedModel(model, quad), blockState, pos, stack, this.chunkBufferBuilderMap.get(quad).getFirst(), false, RandomGenerator.createLegacy(), modelSeed, OverlayTexture.DEFAULT_UV);
				}
			}
		}
	}

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/tasks/ChunkRenderRebuildTask;performBuild(Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationSource;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;", at = @At("RETURN"))
	private void limlib$performBuild$tail(ChunkBuildContext buildContext, CancellationSource cancellationSource, CallbackInfoReturnable<ChunkBuildResult> ci) {
		((ChunkBuildResultAccess) ci.getReturnValue()).getBufferMap().putAll(this.chunkBufferBuilderMap);
		this.chunkBufferBuilderMap.clear();
	}

	@Unique
	private BufferBuilder limlib$makeBufferBuilder() {
		BufferBuilder builder = new BufferBuilder(128);
		builder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
		return builder;
	}

	@Unique
	private Set<BakedQuad> limlib$getQuads(BakedModel model, BlockState state, BlockPos pos, BlockRenderView world, BlockOcclusionCache cache, RandomGenerator randomGenerator, long seed) {
		Set<BakedQuad> bakedQuads = Sets.newHashSet();

		for (Direction dir : Direction.values()) {
			randomGenerator.setSeed(seed);

			if (cache.shouldDrawSide(state, world, pos, dir)) {
				bakedQuads.addAll(model.getQuads(state, dir, randomGenerator));
			}
		}

		randomGenerator.setSeed(seed);
		bakedQuads.addAll(model.getQuads(state, null, randomGenerator));

		return bakedQuads;
	}

}
