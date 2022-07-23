package net.ludocrypt.limlib.mixin.client.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.api.LiminalEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@ModifyVariable(method = "render", at = @At(value = "STORE", ordinal = 4), ordinal = 2)
	private static float limlib$modifySkyColor(float in) {
		MinecraftClient client = MinecraftClient.getInstance();

		LiminalEffects effects = ((DimensionTypeAccess) (Object) client.world.getDimension()).getLiminalEffects();

		if (effects.getSkyShading().isPresent()) {
			return effects.getSkyShading().get();
		}

		return 1.0F;
	}
}
