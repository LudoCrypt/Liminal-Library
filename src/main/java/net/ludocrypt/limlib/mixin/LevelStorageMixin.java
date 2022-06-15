package net.ludocrypt.limlib.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;

import net.ludocrypt.limlib.api.LiminalWorld;
import net.ludocrypt.limlib.impl.LevelStorageHacks;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(LevelStorage.class)
public class LevelStorageMixin {

	@ModifyVariable(method = "Lnet/minecraft/world/level/storage/LevelStorage;readGeneratorProperties(Lcom/mojang/serialization/Dynamic;Lcom/mojang/datafixers/DataFixer;I)Lcom/mojang/datafixers/util/Pair;", at = @At(value = "STORE"), ordinal = 1)
	private static <T> Dynamic<T> limlib$readGeneratorProperties$datafix(Dynamic<T> in, Dynamic<T> levelData, DataFixer dataFixer, int version) {
		Dynamic<T> dynamic = in;
		if (LevelStorageHacks.earlyDynamicRegistryManager != null) {
			for (LiminalWorld world : LimlibRegistries.LIMINAL_WORLD) {
				dynamic = limlib$addDimension(world, dynamic, world.getDimensionOptionsGetter().get(world, BuiltinRegistries.DIMENSION_TYPE, BuiltinRegistries.BIOME, BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.CHUNK_GENERATOR_SETTINGS, BuiltinRegistries.NOISE_PARAMETERS));
			}
		}
		return dynamic;
	}

	@Unique
	@SuppressWarnings("unchecked")
	private static <T> Dynamic<T> limlib$addDimension(LiminalWorld world, Dynamic<T> in, DimensionOptions dimension) {
		Dynamic<T> dimensions = in.get("dimensions").orElseEmptyMap();
		if (!dimensions.get(world.getIdentifier().toString()).result().isPresent()) {
			Map<Dynamic<T>, Dynamic<T>> dimensionsMap = Maps.newHashMap(dimensions.getMapValues().result().get());
			dimensionsMap.put(dimensions.createString(world.getIdentifier().toString()), new Dynamic<T>(dimensions.getOps(), (T) DimensionOptions.CODEC.encodeStart(RegistryOps.of(NbtOps.INSTANCE, LevelStorageHacks.earlyDynamicRegistryManager), dimension).result().get()));
			in = in.set("dimensions", in.createMap(dimensionsMap));
		}
		return in;
	}

}
