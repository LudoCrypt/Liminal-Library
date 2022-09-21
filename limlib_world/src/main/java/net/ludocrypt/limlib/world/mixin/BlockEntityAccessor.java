package net.ludocrypt.limlib.world.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {

	@Invoker
	void callWriteNbt(NbtCompound nbt);

}
