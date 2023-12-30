package net.ludocrypt.limlib.api.world.nbt;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;

import net.ludocrypt.limlib.impl.mixin.NbtCompoundAccessor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class ImmutableNbtCompound extends NbtCompound {

	public ImmutableNbtCompound(NbtCompound from) {
		super(ImmutableMap.copyOf(((NbtCompoundAccessor) from).getEntries()));
	}

	@Override
	@Deprecated
	public NbtElement put(String key, NbtElement nbt) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putBoolean(String key, boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putByte(String key, byte value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putByteArray(String key, byte[] value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putByteArray(String key, List<Byte> value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putDouble(String key, double value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putFloat(String key, float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putInt(String key, int value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putIntArray(String key, int[] value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putIntArray(String key, List<Integer> value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putLong(String key, long value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putLongArray(String key, List<Long> value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putLongArray(String key, long[] value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putShort(String key, short value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putString(String key, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putUuid(String key, UUID value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void remove(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ImmutableNbtCompound getCompound(String key) {
		return new ImmutableNbtCompound(super.getCompound(key));
	}

	@Override
	public ImmutableNbtList getList(String key, int type) {
		return new ImmutableNbtList(super.getList(key, type));
	}

	@Override
	public NbtElement get(String key) {
		NbtElement element = super.get(key);
		return element instanceof NbtCompound ? new ImmutableNbtCompound((NbtCompound) element)
				: element instanceof NbtList ? new ImmutableNbtList((NbtList) element) : element;
	}

	@Override
	public byte[] getByteArray(String key) {
		byte[] in = super.getByteArray(key);
		byte[] bs = new byte[in.length];
		System.arraycopy(in, 0, bs, 0, in.length);
		return bs;
	}

	@Override
	public int[] getIntArray(String key) {
		int[] in = super.getIntArray(key);
		int[] bs = new int[in.length];
		System.arraycopy(in, 0, bs, 0, in.length);
		return bs;
	}

	@Override
	public long[] getLongArray(String key) {
		long[] in = super.getLongArray(key);
		long[] bs = new long[in.length];
		System.arraycopy(in, 0, bs, 0, in.length);
		return bs;
	}

}
