package net.ludocrypt.limlib.api.sound;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public interface SingleSoundEmitter extends SoundEmitter {

	public default Vec3d getOffset(MinecraftClient client, World world, BlockPos pos, BlockState state) {
		return Vec3d.ofCenter(Vec3i.ZERO);
	}

	public SoundEvent getSound(MinecraftClient client, World world, BlockPos pos, BlockState state);

	public default float getPitch(MinecraftClient client, World world, BlockPos pos, BlockState state) {
		return 1.0F;
	}

	public default float getVolume(MinecraftClient client, World world, BlockPos pos, BlockState state) {
		return 1.0F;
	}

	public default float getChance(MinecraftClient client, World world, BlockPos pos, BlockState state) {
		return 1.0F;
	}

	public default SoundCategory getCategory(MinecraftClient client, World world, BlockPos pos, BlockState state) {
		return SoundCategory.AMBIENT;
	}

	public default void playSound(MinecraftClient client, World world, BlockPos pos, BlockState state) {
		if (world.getRandom().nextFloat() < this.getChance(client, world, pos, state)) {
			Vec3d offset = getOffset(client, world, pos, state);
			client.getSoundManager().play(new PositionedSoundInstance(getSound(client, world, pos, state), getCategory(client, world, pos, state), getVolume(client, world, pos, state), getPitch(client, world, pos, state), 0, pos.getX() + offset.getX(), pos.getY() + offset.getY()));
		}
	}

}
