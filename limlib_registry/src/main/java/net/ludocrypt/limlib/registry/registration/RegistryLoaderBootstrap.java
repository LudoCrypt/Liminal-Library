package net.ludocrypt.limlib.registry.registration;

import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;

/**
 * Bootstrap Registration for non json registered things.
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with
 * {@value #ENTRYPOINT_KEY} key.
 * <p>
 */
public interface RegistryLoaderBootstrap {

	/**
	 * Represents the key which this entrypoint is defined with, whose value is
	 * {@value}.
	 */
	public static String ENTRYPOINT_KEY = "limlib:registry_loader_bootstrap";

	/**
	 * Registers everything.
	 */
	void register(RegistryOps.RegistryInfoLookup infoLookup, RegistryKey<? extends Registry<?>> registryKey, MutableRegistry<?> registry);

}
