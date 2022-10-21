package net.ludocrypt.limlib.render.access.sodium;

import java.util.Map;

import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.Identifier;

public interface RenderSectionAccess {

	public Map<BakedQuad, Pair<VertexBuffer, Identifier>> getVertexBufferMap();

}
