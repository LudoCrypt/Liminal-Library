package net.ludocrypt.limlib.impl.mixin;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(NbtList.class)
public interface NbtListAccessor {

	@Accessor
	List<NbtElement> getValue();

	@Mutable
	@Accessor
	void setValue(List<NbtElement> value);

	@Accessor
	byte getType();

	@Accessor
	void setType(byte type);

}
