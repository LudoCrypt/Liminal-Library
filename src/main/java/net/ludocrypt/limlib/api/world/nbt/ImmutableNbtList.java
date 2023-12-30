package net.ludocrypt.limlib.api.world.nbt;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableList;

import net.ludocrypt.limlib.impl.mixin.NbtListAccessor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class ImmutableNbtList extends NbtList {

	public ImmutableNbtList(NbtList from) {
		super();
		((NbtListAccessor) this).setValue(ImmutableList.copyOf(((NbtListAccessor) this).getValue()));
		((NbtListAccessor) this).setType(((NbtListAccessor) this).getType());
	}

	@Override
	@Deprecated
	public boolean addAll(Collection<? extends NbtElement> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void add(int i, NbtElement nbtElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public boolean add(NbtElement e) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends NbtElement> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public boolean addElement(int index, NbtElement nbt) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public NbtElement remove(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public boolean removeIf(Predicate<? super NbtElement> filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	protected void removeRange(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public NbtElement set(int i, NbtElement nbtElement) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public boolean setElement(int index, NbtElement nbt) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void sort(Comparator<? super NbtElement> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public NbtElement get(int i) {
		NbtElement element = super.get(i);
		return element instanceof NbtCompound ? new ImmutableNbtCompound((NbtCompound) element)
				: element instanceof NbtList ? new ImmutableNbtList((NbtList) element) : element;
	}

	@Override
	public NbtCompound getCompound(int index) {
		return new ImmutableNbtCompound(super.getCompound(index));
	}

	@Override
	public NbtList getList(int index) {
		return new ImmutableNbtList(super.getList(index));
	}

	@Override
	public int[] getIntArray(int index) {
		int[] in = super.getIntArray(index);
		int[] bs = new int[in.length];
		System.arraycopy(in, 0, bs, 0, in.length);
		return bs;
	}

	@Override
	public long[] getLongArray(int index) {
		long[] in = super.getLongArray(index);
		long[] bs = new long[in.length];
		System.arraycopy(in, 0, bs, 0, in.length);
		return bs;
	}

}
