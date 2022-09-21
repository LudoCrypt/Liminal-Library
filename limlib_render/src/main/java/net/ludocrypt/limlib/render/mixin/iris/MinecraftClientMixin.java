package net.ludocrypt.limlib.render.mixin.iris;

import org.spongepowered.asm.mixin.Mixin;

import net.coderbot.iris.pipeline.HandRenderer;
import net.irisshaders.iris.api.v0.IrisApi;
import net.ludocrypt.limlib.render.access.IrisClientAccess;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IrisClientAccess {

	@Override
	public boolean areShadersInUse() {
		return IrisApi.getInstance().isShaderPackInUse();
	}

	@Override
	public boolean isHandRenderingActive() {
		return HandRenderer.INSTANCE.isActive();
	}

}
