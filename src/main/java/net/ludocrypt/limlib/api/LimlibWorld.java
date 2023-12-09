package net.ludocrypt.limlib.api;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Lifecycle;

import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.registry.HolderProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class LimlibWorld {

	public static final RegistryKey<Registry<LimlibWorld>> LIMLIB_WORLD_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib", "limlib_world"));
	public static final Registry<LimlibWorld> LIMLIB_WORLD = RegistriesAccessor
		.callRegisterSimple(LIMLIB_WORLD_KEY, Lifecycle.stable(), registry -> new LimlibWorld(() -> null, (r) -> null));

	private Supplier<DimensionType> dimensionTypeSupplier;
	private Function<RegistryProvider, DimensionOptions> dimensionOptionsSupplier;

	public LimlibWorld(Supplier<DimensionType> dimensionTypeSupplier,
			Function<RegistryProvider, DimensionOptions> dimensionOptionsSupplier) {
		this.dimensionTypeSupplier = Suppliers.memoize(dimensionTypeSupplier);
		this.dimensionOptionsSupplier = dimensionOptionsSupplier;
	}

	public Supplier<DimensionType> getDimensionTypeSupplier() {
		return dimensionTypeSupplier;
	}

	public Function<RegistryProvider, DimensionOptions> getDimensionOptionsSupplier() {
		return dimensionOptionsSupplier;
	}

	public static interface RegistryProvider {

		public <T> HolderProvider<T> get(RegistryKey<Registry<T>> key);

	}

	// Load the class early so our variables are set
	public static void load() {

	}

}
