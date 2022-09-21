package net.ludocrypt.limlib.effects.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

@Mixin(Registry.class)
public interface RegistryAccessor {

	@Invoker
	static <T> RegistryKey<Registry<T>> callCreateRegistryKey(String registryId) {
		throw new UnsupportedOperationException();
	}

}
