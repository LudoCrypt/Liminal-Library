package net.ludocrypt.limlib.registry.mixin;

import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;

import net.ludocrypt.limlib.registry.registration.LimlibWorld;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryOps;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(LevelStorage.class)
public class LevelStorageMixin {

	@ModifyVariable(method = "Lnet/minecraft/world/level/storage/LevelStorage;readGeneratorProperties(Lcom/mojang/serialization/Dynamic;Lcom/mojang/datafixers/DataFixer;I)Lcom/mojang/datafixers/util/Pair;", at = @At(value = "STORE"), ordinal = 1)
	private static <T> Dynamic<T> limlib$readGeneratorProperties$datafix(Dynamic<T> in, Dynamic<T> levelData, DataFixer dataFixer, int version) {
		Dynamic<T> dynamic = in;
		for (Entry<RegistryKey<LimlibWorld>, LimlibWorld> entry : LimlibWorld.LIMLIB_WORLD.getEntries()) {
			dynamic = limlib$addDimension(entry.getKey(), entry.getValue(), dynamic);
		}
		return dynamic;
	}

	@Unique
	@SuppressWarnings("unchecked")
	private static <T> Dynamic<T> limlib$addDimension(RegistryKey<LimlibWorld> key, LimlibWorld world, Dynamic<T> in) {
		Dynamic<T> dimensions = in.get("dimensions").orElseEmptyMap();
		if (!dimensions.get(key.getValue().toString()).result().isPresent()) {
			Map<Dynamic<T>, Dynamic<T>> dimensionsMap = Maps.newHashMap(dimensions.getMapValues().result().get());
			dimensionsMap.put(dimensions.createString(key.getValue().toString()), new Dynamic<T>(dimensions.getOps(), (T) DimensionOptions.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, BuiltinRegistries.MANAGER), world.getDimensionOptionsSupplier().get()).result().get()));
			in = in.set("dimensions", in.createMap(dimensionsMap));
		}
		return in;
	}

}
