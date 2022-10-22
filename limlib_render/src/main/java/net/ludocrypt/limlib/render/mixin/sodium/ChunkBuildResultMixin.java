package net.ludocrypt.limlib.render.mixin.sodium;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.datafixers.util.Pair;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildResult;
import net.ludocrypt.limlib.render.access.sodium.ChunkBuildResultAccess;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

@Mixin(ChunkBuildResult.class)
public class ChunkBuildResultMixin implements ChunkBuildResultAccess {

	@Unique
	private Map<BakedQuad, Pair<BufferBuilder, Identifier>> bufferMap = Maps.newHashMap();

	@Override
	public Map<BakedQuad, Pair<BufferBuilder, Identifier>> getBufferMap() {
		return bufferMap;
	}

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildResult;delete()V", at = @At("HEAD"), remap = false)
	private void delete(CallbackInfo ci) {
		this.getBufferMap().forEach((quad, renderPair) -> renderPair.getFirst().clear());
		this.getBufferMap().clear();
	}

}
