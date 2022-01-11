package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.ludocrypt.limlib.access.ClientPlayNetworkHandlerAccess;
import net.ludocrypt.limlib.api.sound.TravelSoundPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.network.NetworkThreadUtils;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerAccess {

	@Override
	public void handleTravelSound(TravelSoundPacket travelSound) {
		MinecraftClient client = MinecraftClient.getInstance();
		NetworkThreadUtils.forceMainThread(travelSound, ((ClientPlayNetworkHandler) (Object) this), client);
		if (client != null) {
			client.getSoundManager().play(PositionedSoundInstance.ambient(travelSound.getSound(), client.world.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F));
		}
	}

}
