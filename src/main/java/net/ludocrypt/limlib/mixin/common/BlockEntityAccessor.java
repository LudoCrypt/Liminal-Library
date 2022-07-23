package net.ludocrypt.limlib.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {

	@Invoker
	void callWriteNbt(NbtCompound nbt);

}
