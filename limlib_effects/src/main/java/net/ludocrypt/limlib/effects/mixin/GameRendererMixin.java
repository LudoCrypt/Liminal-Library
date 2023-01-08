package net.ludocrypt.limlib.effects.mixin;

import java.util.Optional;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.ludocrypt.limlib.effects.LimlibEffects;
import net.ludocrypt.limlib.effects.post.PostEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.world.World;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Unique
	private final Function<RegistryKey<World>, Optional<ManagedShaderEffect>> memoizedShaders = Util.memoize((world) -> {
		Optional<PostEffect> optionalPostEffect = LimlibEffects.snatch(client.world.getRegistryManager().getLookup(PostEffect.POST_EFFECT_KEY).get(), RegistryKey.of(PostEffect.POST_EFFECT_KEY, client.world.getRegistryKey().getValue()));
		if (optionalPostEffect.isPresent()) {
			if (optionalPostEffect.get().getShaderLocation() != null) {
				return Optional.of(ShaderEffectManager.getInstance().manage(optionalPostEffect.get().getShaderLocation()));
			}
		}
		return Optional.empty();
	});

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = Shift.AFTER))
	private void limlib$render(float tickDelta, long nanoTime, boolean renderLevel, CallbackInfo info) {
		Optional<PostEffect> optionalPostEffect = LimlibEffects.snatch(client.world.getRegistryManager().getLookup(PostEffect.POST_EFFECT_KEY).get(), RegistryKey.of(PostEffect.POST_EFFECT_KEY, client.world.getRegistryKey().getValue()));
		if (optionalPostEffect.isPresent()) {
			PostEffect postEffect = optionalPostEffect.get();
			if (postEffect.shouldRender()) {
				Optional<ManagedShaderEffect> shaderEffect = memoizedShaders.apply(client.world.getRegistryKey());
				if (shaderEffect.isPresent()) {
					postEffect.beforeRender();
					shaderEffect.get().render(tickDelta);
				}
			}
		}
	}

}
