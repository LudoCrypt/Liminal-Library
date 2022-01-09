package net.ludocrypt.limlib.api.sound;

import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface RandomSoundEmitter {

	public SoundEvent getSound(World world, BlockState state);

	public Vec3d getRelativePos(World world, BlockState state);

	public double getChance(World world, BlockState state);

	public float getVolume(World world, BlockState state);

	public float getPitch(World world, BlockState state);

}
