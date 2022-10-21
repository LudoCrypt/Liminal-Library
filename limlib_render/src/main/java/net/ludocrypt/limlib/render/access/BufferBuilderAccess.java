package net.ludocrypt.limlib.render.access;

import java.util.Map;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

public interface BufferBuilderAccess {

	public Map<BakedQuad, Pair<BufferBuilder, Identifier>> getBufferMap();

}
