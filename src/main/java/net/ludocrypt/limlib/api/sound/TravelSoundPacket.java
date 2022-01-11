package net.ludocrypt.limlib.api.sound;

import net.ludocrypt.limlib.access.ClientPlayNetworkHandlerAccess;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class TravelSoundPacket implements Packet<ClientPlayPacketListener> {
	private final SoundEvent sound;

	public TravelSoundPacket(SoundEvent sound) {
		this.sound = sound;

	}

	public TravelSoundPacket(PacketByteBuf buf) {
		this.sound = Registry.SOUND_EVENT.get(buf.readIdentifier());
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeIdentifier(this.sound.getId());
	}

	@Override
	public void apply(ClientPlayPacketListener clientPlayPacketListener) {
		if (clientPlayPacketListener instanceof ClientPlayNetworkHandlerAccess) {
			((ClientPlayNetworkHandlerAccess) clientPlayPacketListener).handleTravelSound(this);
		}
	}

	public SoundEvent getSound() {
		return sound;
	}

}
