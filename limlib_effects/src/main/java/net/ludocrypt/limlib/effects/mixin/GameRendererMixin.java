package net.ludocrypt.limlib.effects.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.effects.render.post.PostEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = Shift.AFTER))
	private void movementVision$render(float tickDelta, long nanoTime, boolean renderLevel, CallbackInfo info) {
		Optional<PostEffect> optionalPostEffect = PostEffect.POST_EFFECT.getOrEmpty(client.world.getRegistryKey().getValue());
		if (optionalPostEffect.isPresent()) {
			PostEffect postEffect = optionalPostEffect.get();
			if (postEffect.shouldRender() && postEffect.getShaderLocation() != null && postEffect.getMemoizedShaderEffect().get() != null) {
				postEffect.beforeRender();
				postEffect.getMemoizedShaderEffect().get().render(tickDelta);
			}
		}
	}

}
