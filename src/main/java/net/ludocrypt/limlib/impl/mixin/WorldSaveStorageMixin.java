package net.ludocrypt.limlib.impl.mixin;

import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.common.collect.Maps;
import com.mojang.serialization.Dynamic;

import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.api.LimlibWorld.RegistryProvider;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.HolderProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.world.FeatureAndDataSettings;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.storage.WorldSaveStorage;

@Mixin(WorldSaveStorage.class)
public class WorldSaveStorageMixin {

	@ModifyVariable(method = "Lnet/minecraft/world/storage/WorldSaveStorage;method_54523(Lcom/mojang/serialization/Dynamic;Lnet/minecraft/server/world/FeatureAndDataSettings;Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/DynamicRegistryManager$Frozen;)Lnet/minecraft/unmapped/C_qhzfxdhh;", at = @At(value = "STORE"), ordinal = 2)
	private static <T> Dynamic<T> limlib$readGeneratorProperties$datafix(Dynamic<T> in, Dynamic<?> levelData,
			FeatureAndDataSettings featureAndDataSettings, Registry<DimensionOptions> registry,
			DynamicRegistryManager.Frozen frozen) {
		Dynamic<T> dynamic = in;

		for (Entry<RegistryKey<LimlibWorld>, LimlibWorld> entry : LimlibWorld.LIMLIB_WORLD.getEntries()) {
			dynamic = limlib$addDimension(entry.getKey(), entry.getValue(), dynamic, frozen);
		}

		return dynamic;
	}

	@Unique
	@SuppressWarnings("unchecked")
	private static <T> Dynamic<T> limlib$addDimension(RegistryKey<LimlibWorld> key, LimlibWorld world, Dynamic<T> in,
			DynamicRegistryManager registryManager) {
		Dynamic<T> dimensions = in.get("dimensions").orElseEmptyMap();

		if (!dimensions.get(key.getValue().toString()).result().isPresent()) {
			Map<Dynamic<T>, Dynamic<T>> dimensionsMap = Maps.newHashMap(dimensions.getMapValues().result().get());

			dimensionsMap
				.put(dimensions.createString(key.getValue().toString()),
					new Dynamic<T>(dimensions.getOps(),
						(T) DimensionOptions.CODEC
							.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registryManager),
								world.getDimensionOptionsSupplier().apply(new RegistryProvider() {

									@Override
									public <Q> HolderProvider<Q> get(RegistryKey<Registry<Q>> key) {
										return registryManager.getLookup(key).get();
									}

								}))
							.result()
							.get()));
			return in.set("dimensions", in.createMap(dimensionsMap));
		}

		return in;
	}

}
