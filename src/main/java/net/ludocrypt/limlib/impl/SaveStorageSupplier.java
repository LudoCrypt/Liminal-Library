package net.ludocrypt.limlib.impl;

import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.registry.DynamicRegistryManager;

public class SaveStorageSupplier {

	public static final AtomicReference<DynamicRegistryManager.Frozen> LOADED_REGISTRY = new AtomicReference<DynamicRegistryManager.Frozen>();

}
