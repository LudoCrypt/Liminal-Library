package net.ludocrypt.limlib.render.mixin.chunk;

import java.util.Map;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.BufferBuilder;

import net.ludocrypt.limlib.render.LimlibRender;
import net.ludocrypt.limlib.render.access.BlockBufferBuilderStorageAccess;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;

@Mixin(BlockBufferBuilderStorage.class)
public class BlockBufferBuilderStorageMixin implements BlockBufferBuilderStorageAccess {

	@Unique
	private final Map<SpecialModelRenderer, BufferBuilder> specialModelBuffers = LimlibRender.SPECIAL_MODEL_RENDERERS.keySet().stream().collect(Collectors.toMap(renderer -> renderer, renderer -> new BufferBuilder(256)));

	@Override
	public BufferBuilder get(SpecialModelRenderer modelRenderer) {
		return this.specialModelBuffers.get(modelRenderer);
	}

	@Override
	public Map<SpecialModelRenderer, BufferBuilder> getSpecialModelBuffers() {
		return specialModelBuffers;
	}

	@Inject(method = "clear", at = @At("TAIL"))
	private void limlib$clear(CallbackInfo ci) {
		this.specialModelBuffers.values().forEach(BufferBuilder::clear);
	}

	@Inject(method = "reset", at = @At("TAIL"))
	private void limlib$reset(CallbackInfo ci) {
		this.specialModelBuffers.values().forEach(BufferBuilder::discard);
	}

}
