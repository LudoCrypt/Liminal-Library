package net.ludocrypt.limlib.api.sound;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface MultipleSoundEmitter {

	public List<SingleSoundEmitter> getEmitters(MinecraftClient client, World world, BlockPos pos, BlockState state);

	public default void playSounds(MinecraftClient client, World world, BlockPos pos, BlockState state) {
		this.getEmitters(client, world, pos, state).forEach((emitter) -> emitter.playSound(client, world, pos, state));
	}

}
