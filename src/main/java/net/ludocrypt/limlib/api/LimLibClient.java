package net.ludocrypt.limlib.api;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.ludocrypt.limlib.api.sound.RandomSoundEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.ChunkStatus;

public class LimLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_WORLD_TICK.register((world) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			ClientPlayerEntity player = client.player;
			Iterable<BlockPos.Mutable> iterable = BlockPos.iterateInSquare(player.getBlockPos(), 8, Direction.DOWN, Direction.NORTH);
			for (BlockPos.Mutable mutable : iterable) {
				BlockPos pos = mutable.toImmutable();
				if (pos.isWithinDistance(player.getBlockPos(), 16)) {
					if (world.getChunk(pos).getStatus().isAtLeast(ChunkStatus.FULL)) {
						BlockState state = world.getBlockState(pos);
						if (state != null) {
							if (state.getBlock()instanceof RandomSoundEmitter rse) {
								if (world.getRandom().nextDouble() < rse.getChance(world, state)) {
									Vec3d relativePos = rse.getRelativePos(world, state);
									world.playSound(pos.getX() + relativePos.getX(), pos.getY() + relativePos.getY(), pos.getZ() + relativePos.getZ(), rse.getSound(world, state), SoundCategory.AMBIENT, rse.getVolume(world, state), rse.getPitch(world, state), true);
								}
							}
						}
					}
				}
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(new Identifier("limlib", "travel_sound"), (client, handler, buf, responseSender) -> {
			SoundEvent sound = Registry.SOUND_EVENT.get(buf.readIdentifier());
			client.execute(() -> client.getSoundManager().play(PositionedSoundInstance.ambient(sound, client.world.getRandom().nextFloat() * 0.4F + 0.8F, 0.25F)));
		});
	}

}
