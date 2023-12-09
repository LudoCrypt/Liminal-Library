package net.ludocrypt.limlib.impl.access;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public interface TagGroupLoaderAccess {

	public Optional<RegistryKey<? extends Registry<?>>> getRegistryKey();

	public void setRegistryKey(@Nullable RegistryKey<? extends Registry<?>> key);

}
