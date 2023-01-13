package net.ludocrypt.limlib.registry.registration;

import java.util.concurrent.atomic.AtomicReference;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.HolderProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

/**
 * A separate implementation of a world and dimension.
 */
public class LimlibWorld {

	public static final SimpleRegistry<LimlibWorld> LIMLIB_WORLD = FabricRegistryBuilder.createSimple(LimlibWorld.class, new Identifier("limlib", "limlib_world")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private Supplier<DimensionType> dimensionTypeSupplier;
	private Function<RegistryProvider, DimensionOptions> dimensionOptionsSupplier;

	public static final AtomicReference<DynamicRegistryManager.Frozen> LOADED_REGISTRY = new AtomicReference<DynamicRegistryManager.Frozen>();

	public LimlibWorld(Supplier<DimensionType> dimensionTypeSupplier, Function<RegistryProvider, DimensionOptions> dimensionOptionsSupplier) {
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

}
