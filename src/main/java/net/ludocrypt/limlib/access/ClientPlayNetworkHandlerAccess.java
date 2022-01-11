package net.ludocrypt.limlib.access;

import net.ludocrypt.limlib.api.sound.TravelSoundPacket;

public interface ClientPlayNetworkHandlerAccess {

	public default void handleTravelSound(TravelSoundPacket travelSound) {
	}

}
