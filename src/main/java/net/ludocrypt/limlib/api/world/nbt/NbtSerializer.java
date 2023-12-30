package net.ludocrypt.limlib.api.world.nbt;

import net.ludocrypt.limlib.impl.mixin.NbtCompoundAccessor;
import net.minecraft.nbt.NbtCompound;

public interface NbtSerializer<T> {

	public ImmutableNbtCompound write();

	public default void write(NbtCompound nbt) {
		((NbtCompoundAccessor) nbt).getEntries().putAll(((NbtCompoundAccessor) write()).getEntries());
	}

	public T read(ImmutableNbtCompound nbt);

}
