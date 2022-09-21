package net.ludocrypt.limlib.effects.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.ludocrypt.limlib.effects.render.sky.SkyEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@ModifyVariable(method = "render", at = @At(value = "STORE", ordinal = 3), ordinal = 2)
	private static float limlib$modifySkyColor(float in) {
		MinecraftClient client = MinecraftClient.getInstance();

		Optional<SkyEffects> sky = SkyEffects.SKY_EFFECTS.getOrEmpty(client.world.getRegistryKey().getValue());
		if (sky.isPresent()) {
			return sky.get().getSkyShading();
		}

		return in;
	}
}
