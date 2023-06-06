package net.ludocrypt.limlib.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.ApiStatus.Internal;

import com.google.common.collect.Maps;

import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps.RegistryInfoLookup;

public class LimlibRegistryHooks {

	@Internal
	public static final Map<RegistryKey<? extends Registry<?>>, List<LimlibRegistryHook<?>>> REGISTRY_HOOKS = Maps.newHashMap();

	public static <O, T extends Registry<O>> void hook(RegistryKey<T> key, LimlibRegistryHook<O> hook) {
		List<LimlibRegistryHook<?>> hooks = REGISTRY_HOOKS.computeIfAbsent(key, k -> new ArrayList<>());
		hooks.add(hook);
	}

	@FunctionalInterface
	public interface LimlibRegistryHook<O> {

		/**
		 * @param infoLookup  The full registry lookup.
		 * @param registryKey The RegistryKey of the registry.
		 * @param registry    The MutableRegistry where to register.
		 */
		void register(RegistryInfoLookup infoLookup, RegistryKey<? extends Registry<O>> registryKey, MutableRegistry<O> registry);

	}

}
