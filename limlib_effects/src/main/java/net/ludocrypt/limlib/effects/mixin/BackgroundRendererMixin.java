package net.ludocrypt.limlib.effects.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.ludocrypt.limlib.effects.LimlibEffects;
import net.ludocrypt.limlib.effects.sky.DimensionEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.registry.RegistryKey;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@ModifyVariable(method = "render", at = @At(value = "STORE", ordinal = 3), ordinal = 2)
	private static float limlib$modifySkyColor(float in) {
		MinecraftClient client = MinecraftClient.getInstance();

		Optional<DimensionEffects> dimensionEffects = LimlibEffects.snatch(client.world.getRegistryManager().getLookup(DimensionEffects.DIMENSION_EFFECTS_KEY).get(), RegistryKey.of(DimensionEffects.DIMENSION_EFFECTS_KEY, client.world.getRegistryKey().getValue()));
		if (dimensionEffects.isPresent()) {
			return dimensionEffects.get().getSkyShading();
		}

		return in;
	}
}
