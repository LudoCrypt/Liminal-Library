package net.ludocrypt.limlib.render.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.vertex.BufferBuilder;

@Mixin(BufferBuilder.class)
public interface BufferBuilderAccessor {

	@Accessor
	int getVertexCount();

	@Accessor
	boolean getIndexOnly();

}
