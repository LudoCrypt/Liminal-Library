package net.ludocrypt.limlib.api.world.maze.storage;

import net.minecraft.nbt.NbtCompound;

public interface NbtSerializer<T> {

	public NbtCompound write(NbtCompound nbt);

	public T read(NbtCompound nbt);

}
