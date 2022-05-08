package net.ludocrypt.limlib.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.MusicSound;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	public ClientPlayerEntity player;

	@Shadow
	public ClientWorld world;

	@Inject(method = "getMusicType", at = @At("HEAD"), cancellable = true)
	private void limlib$getMusicType(CallbackInfoReturnable<MusicSound> ci) {
		if (this.player != null) {
			Optional<MusicSound> optional = ((DimensionEffectsAccess) this.world.getDimension()).getLiminalEffects().getMusic();
			if (optional.isPresent()) {
				ci.setReturnValue(optional.get());
			}
		}
	}

}
