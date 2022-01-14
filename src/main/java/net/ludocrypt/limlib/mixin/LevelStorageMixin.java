package net.ludocrypt.limlib.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;

import net.ludocrypt.limlib.api.world.LevelStorageHacks;
import net.ludocrypt.limlib.api.world.LiminalWorld;
import net.ludocrypt.limlib.impl.world.LiminalDimensions;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(LevelStorage.class)
public class LevelStorageMixin {

	@ModifyVariable(method = "readGeneratorProperties", at = @At(value = "STORE"), ordinal = 1)
	private static <T> Dynamic<T> limlib$readGeneratorProperties$datafix(Dynamic<T> in, Dynamic<T> levelData, DataFixer dataFixer, int version) {
		Dynamic<T> dynamic = in;
		if (LevelStorageHacks.earlyDynamicRegistryManager != null) {
			for (LiminalWorld world : LiminalDimensions.LIMINAL_WORLD_REGISTRY) {
				dynamic = limlib$addDimension(world, dynamic, world.worldDimensionOptions.apply(LevelStorageHacks.earlyDynamicRegistryManager.get(Registry.DIMENSION_TYPE_KEY), LevelStorageHacks.earlyDynamicRegistryManager.get(Registry.BIOME_KEY), in.get("seed").asLong(0)));
			}
		}
		return dynamic;
	}

	@Unique
	@SuppressWarnings("unchecked")
	private static <T> Dynamic<T> limlib$addDimension(LiminalWorld world, Dynamic<T> in, DimensionOptions dimension) {
		Dynamic<T> dimensions = in.get("dimensions").orElseEmptyMap();
		if (!dimensions.get(world.worldId.toString()).result().isPresent()) {
			Map<Dynamic<T>, Dynamic<T>> dimensionsMap = Maps.newHashMap(dimensions.getMapValues().result().get());
			dimensionsMap.put(dimensions.createString(world.worldId.toString()), new Dynamic<T>(dimensions.getOps(), (T) DimensionOptions.CODEC.encodeStart(RegistryReadingOps.of(NbtOps.INSTANCE, LevelStorageHacks.earlyDynamicRegistryManager), dimension).result().get()));
			in = in.set("dimensions", in.createMap(dimensionsMap));
		}
		return in;
	}

}
