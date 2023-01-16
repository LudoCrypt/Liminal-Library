package net.ludocrypt.limlib.render.mixin.chunk;

import java.util.Map;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.VertexBuffer;

import net.ludocrypt.limlib.render.access.BuiltChunkAccess;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;

@Mixin(BuiltChunk.class)
public class BuiltChunkMixin implements BuiltChunkAccess {

	@Shadow
	@Final
	ChunkBuilder f_dssekupm;

	@Unique
	private final Map<SpecialModelRenderer, VertexBuffer> specialModelBuffers = SpecialModelRenderer.SPECIAL_MODEL_RENDERER.getEntries().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> new VertexBuffer()));

	@Override
	public VertexBuffer getBuffer(SpecialModelRenderer modelRenderer) {
		return specialModelBuffers.get(modelRenderer);
	}

	@Override
	public Map<SpecialModelRenderer, VertexBuffer> getSpecialModelBuffers() {
		return specialModelBuffers;
	}

	@Override
	public ChunkBuilder getSuperChunkBuilder() {
		return this.f_dssekupm;
	}

	@Inject(method = "delete", at = @At("TAIL"))
	private void limlib$delete(CallbackInfo ci) {
		this.specialModelBuffers.values().forEach(VertexBuffer::close);
	}

}
