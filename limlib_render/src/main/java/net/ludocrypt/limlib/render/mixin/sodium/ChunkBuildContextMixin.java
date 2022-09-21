package net.ludocrypt.limlib.render.mixin.sodium;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import me.jellysquid.mods.sodium.client.gl.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.ludocrypt.limlib.render.access.ChunkBuildContextAccess;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

@Mixin(ChunkBuildContext.class)
public class ChunkBuildContextMixin implements ChunkBuildContextAccess {

	@Unique
	private Map<BakedQuad, Pair<ChunkBuildBuffers, Identifier>> buildBuffersMap = Maps.newHashMap();

	@Inject(method = "Lme/jellysquid/mods/sodium/client/gl/compile/ChunkBuildContext;release()V", at = @At("TAIL"))
	private void limlib$release(CallbackInfo ci) {
		buildBuffersMap.values().stream().map(Pair::getFirst).forEach(ChunkBuildBuffers::destroy);
	}

	@Override
	public Map<BakedQuad, Pair<ChunkBuildBuffers, Identifier>> getBuildBuffersMap() {
		return buildBuffersMap;
	}

}
