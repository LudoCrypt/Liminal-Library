package net.ludocrypt.limlib.effects.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.effects.sound.SoundEffects;
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
			Optional<SoundEffects> soundEffects = SoundEffects.SOUND_EFFECTS.getOrEmpty(world.getRegistryKey().getValue());
			if (soundEffects.isPresent()) {
				Optional<MusicSound> musicSound = soundEffects.get().getMusic();
				if (musicSound.isPresent()) {
					ci.setReturnValue(musicSound.get());
				}
			}
		}
	}

}
