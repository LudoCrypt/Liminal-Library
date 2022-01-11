package net.ludocrypt.limlib.api.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.network.NetworkThreadUtils;
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
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			MinecraftClient client = MinecraftClient.getInstance();
			NetworkThreadUtils.forceMainThread(this, clientPlayPacketListener, client);
			if (client != null) {
				client.getSoundManager().play(PositionedSoundInstance.ambient(this.sound, client.world.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F));
			}
		}
	}

}
