package net.ludocrypt.limlib.api.sound;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface SoundEmitter {

	public void playSound(MinecraftClient client, World world, BlockPos pos, BlockState state);

}
