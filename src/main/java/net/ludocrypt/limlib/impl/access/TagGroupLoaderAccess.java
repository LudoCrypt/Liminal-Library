package net.ludocrypt.limlib.impl.access;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public interface TagGroupLoaderAccess<O> {

	public Optional<RegistryKey<? extends Registry<O>>> getRegistryKey();

	public void setRegistryKey(@Nullable RegistryKey<? extends Registry<O>> key);

}
