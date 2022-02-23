package net.ludocrypt.limlib.api.sound;

import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface RandomSoundEmitter {

	public SoundEvent getSound(World world, BlockState state, BlockPos pos);

	public Vec3d getRelativePos(World world, BlockState state, BlockPos pos);

	public double getChance(World world, BlockState state, BlockPos pos);

	public float getVolume(World world, BlockState state, BlockPos pos);

	public float getPitch(World world, BlockState state, BlockPos pos);

	public SoundCategory getSoundCategory(World world, BlockState state, BlockPos pos);

}
