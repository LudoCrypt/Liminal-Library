package net.ludocrypt.limlib.render.mixin.registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

@Mixin(BuiltinRegistries.class)
public interface BuiltinRegistriesAccessor {

	@Invoker
	static <T> Registry<T> callAddRegistry(RegistryKey<? extends Registry<T>> registryRef, BuiltinRegistries.RegistryBootstrap<T> bootstrap) {
		throw new UnsupportedOperationException();
	}

}
