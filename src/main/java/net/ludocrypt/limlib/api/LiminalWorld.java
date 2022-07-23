package net.ludocrypt.limlib.api;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class LiminalWorld {

	private final Identifier identifier;
	private final LiminalEffects liminalEffects;
	private final Supplier<DimensionType> dimensionType;
	private final Supplier<DimensionOptions> dimensionOptions;

	public LiminalWorld(Identifier identifier, LiminalEffects liminalEffects, Supplier<DimensionType> dimensionType, Supplier<DimensionOptions> dimensionOptions) {
		this.identifier = identifier;
		this.liminalEffects = liminalEffects;
		this.dimensionType = Suppliers.memoize(() -> {
			DimensionType type = dimensionType.get();
			((DimensionTypeAccess) (Object) type).setLiminalEffects(liminalEffects);
			return type;
		});
		this.dimensionOptions = Suppliers.memoize(dimensionOptions);
	}

	public Identifier getIdentifier() {
		return this.identifier;
	}

	public RegistryKey<DimensionType> getDimensionTypeKey() {
		return RegistryKey.of(Registry.DIMENSION_TYPE_KEY, this.identifier);
	}

	public RegistryKey<DimensionOptions> getDimensionKey() {
		return RegistryKey.of(Registry.DIMENSION_KEY, this.identifier);
	}

	public RegistryKey<World> getWorldKey() {
		return RegistryKey.of(Registry.WORLD_KEY, this.identifier);
	}

	public DimensionType getDimensionType() {
		return this.dimensionType.get();
	}

	public DimensionOptions getDimensionOptions() {
		return this.dimensionOptions.get();
	}

	public LiminalEffects getLiminalEffects() {
		return this.liminalEffects;
	}

}
