package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.sound.SoundExecutor;
import net.minecraft.client.sound.SoundSystem;

@Mixin(SoundSystem.class)
public interface SoundSystemAccessor {

	@Accessor
	SoundExecutor getTaskQueue();

}
