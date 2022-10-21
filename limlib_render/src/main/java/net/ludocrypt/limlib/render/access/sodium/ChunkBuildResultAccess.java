package net.ludocrypt.limlib.render.access.sodium;

import java.util.Map;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

public interface ChunkBuildResultAccess {

	public Map<BakedQuad, Pair<BufferBuilder, Identifier>> getBufferMap();

}
