package net.ludocrypt.limlib.registry.registration;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.HolderProvider;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

/**
 * A separate implementation of a world and dimension.
 */
public class LimlibWorld {

	public static final SimpleRegistry<LimlibWorld> LIMLIB_WORLD = FabricRegistryBuilder.createSimple(LimlibWorld.class, new Identifier("limlib", "limlib_world")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	private Supplier<DimensionType> dimensionTypeSupplier;

	private Function<Pair<HolderProvider<DimensionType>, HolderProvider<Biome>>, DimensionOptions> dimensionOptionsSupplier;

	public LimlibWorld(Supplier<DimensionType> dimensionTypeSupplier, Function<Pair<HolderProvider<DimensionType>, HolderProvider<Biome>>, DimensionOptions> dimensionOptionsSupplier) {
		this.dimensionTypeSupplier = memoize(dimensionTypeSupplier);
		this.dimensionOptionsSupplier = memoize(dimensionOptionsSupplier);
	}

	public Supplier<DimensionType> getDimensionTypeSupplier() {
		return dimensionTypeSupplier;
	}

	public Function<Pair<HolderProvider<DimensionType>, HolderProvider<Biome>>, DimensionOptions> getDimensionOptionsSupplier() {
		return dimensionOptionsSupplier;
	}

	private <T> Supplier<T> memoize(Supplier<T> delegate) {
		return new Supplier<T>() {

			private final AtomicReference<T> cache = new AtomicReference<T>();

			@Override
			public @Nullable T get() {

				if (cache.get() == null) {
					cache.set(delegate.get());
				}

				return cache.get();
			}

		};
	}

	private <T, R> Function<T, R> memoize(Function<T, R> delegate) {
		return new Function<T, R>() {

			private final Map<T, R> cache = Maps.newHashMap();

			@Override
			public R apply(T in) {

				if (!cache.containsKey(in)) {
					cache.put(in, delegate.apply(in));
				}

				return cache.get(in);
			}

		};
	}

}
