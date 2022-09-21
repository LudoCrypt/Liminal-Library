package net.ludocrypt.limlib.render.access;

import java.util.Map;

import com.mojang.datafixers.util.Pair;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

public interface ChunkBuildContextAccess {

	public Map<BakedQuad, Pair<ChunkBuildBuffers, Identifier>> getBuildBuffersMap();

}
