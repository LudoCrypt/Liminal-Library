package net.ludocrypt.limlib.render.mixin.sodium;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.datafixers.util.Pair;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildResult;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegionManager;
import net.ludocrypt.limlib.render.access.sodium.ChunkBuildResultAccess;
import net.ludocrypt.limlib.render.access.sodium.RenderSectionAccess;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

@Mixin(RenderRegionManager.class)
public class RenderRegionManagerMixin {

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/region/RenderRegionManager;upload(Lme/jellysquid/mods/sodium/client/gl/device/CommandList;Lme/jellysquid/mods/sodium/client/render/chunk/region/RenderRegion;Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gl/arena/GlBufferArena;upload(Lme/jellysquid/mods/sodium/client/gl/device/CommandList;Ljava/util/stream/Stream;)Z", ordinal = 1, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$upload(CommandList commandList, RenderRegion region, List<ChunkBuildResult> results, CallbackInfo ci, List<?> sectionUploads, RenderRegion.RenderRegionArenas arenas, boolean bufferChanged) {
		results.forEach((result) -> {
			Map<BakedQuad, Pair<BufferBuilder, Identifier>> chunkBufferBuilderMap = ((ChunkBuildResultAccess) result).getBufferMap();

			((RenderSectionAccess) result.render).getVertexBufferMap().forEach((quad, renderPair) -> renderPair.getFirst().close());
			((RenderSectionAccess) result.render).getVertexBufferMap().clear();

			chunkBufferBuilderMap.forEach((quad, renderPair) -> {
				BufferBuilder.RenderedBuffer renderedBuffer = renderPair.getFirst().endOrDiscard();
				if (renderedBuffer != null) {
					VertexBuffer buffer = new VertexBuffer();

					if (!buffer.invalid()) {
						buffer.bind();
						buffer.upload(renderedBuffer);
						VertexBuffer.unbind();
					}

					((RenderSectionAccess) result.render).getVertexBufferMap().put(quad, Pair.of(buffer, renderPair.getSecond()));
				}
			});
		});
	}

}
