package net.ludocrypt.limlib.render.mixin.registry;

import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.util.Holder;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

@Mixin(Registry.class)
public interface RegistryAccessor {

	@Invoker
	static <T> RegistryKey<Registry<T>> callCreateRegistryKey(String registryId) {
		throw new UnsupportedOperationException();
	}

	@Invoker
	static <T> DefaultedRegistry<T> callRegisterDefaulted(RegistryKey<? extends Registry<T>> key, String id, Function<T, Holder.Reference<T>> holderReference, Registry.RegistryBootstrap<T> bootstrap) {
		throw new UnsupportedOperationException();
	}
}
