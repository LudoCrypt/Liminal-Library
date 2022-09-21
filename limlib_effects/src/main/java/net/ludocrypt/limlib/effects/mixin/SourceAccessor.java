package net.ludocrypt.limlib.effects.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.sound.Source;

@Mixin(Source.class)
public interface SourceAccessor {

	@Accessor
	int getPointer();

}
