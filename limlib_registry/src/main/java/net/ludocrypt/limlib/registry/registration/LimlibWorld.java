package net.ludocrypt.limlib.registry.registration;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
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

	private Supplier<DimensionOptions> dimensionOptionsSupplier;

	public LimlibWorld(Supplier<DimensionType> dimensionTypeSupplier, Supplier<DimensionOptions> dimensionOptionsSupplier) {
		this.dimensionTypeSupplier = Suppliers.memoize(dimensionTypeSupplier);
		this.dimensionOptionsSupplier = Suppliers.memoize(dimensionOptionsSupplier);
	}

	public Supplier<DimensionType> getDimensionTypeSupplier() {
		return dimensionTypeSupplier;
	}

	public Supplier<DimensionOptions> getDimensionOptionsSupplier() {
		return dimensionOptionsSupplier;
	}

}
