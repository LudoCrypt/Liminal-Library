package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;

import net.ludocrypt.limlib.api.world.LevelStorageHacks;
import net.ludocrypt.limlib.api.world.LiminalWorld;
import net.ludocrypt.limlib.impl.world.LiminalDimensions;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;

@SuppressWarnings("deprecation")
@Mixin(LevelStorage.class)
public class LevelStorageMixin {

	@ModifyVariable(method = "readGeneratorProperties", at = @At(value = "STORE"), ordinal = 1)
	private static <T> Dynamic<T> limlib$readGeneratorProperties$datafix(Dynamic<T> in, Dynamic<T> levelData, DataFixer dataFixer, int version) {
		Dynamic<T> dynamic = in;
		if (LevelStorageHacks.earlyDynamicRegistryManager != null) {
			LiminalDimensions.LIMINAL_WORLD_REGISTRY.forEach((world) -> limlib$addDimension(world, in, LevelStorageHacks.earlyDynamicRegistryManager));
		}
		return dynamic;
	}

	@Unique
	private static <T> Dynamic<T> limlib$addDimension(LiminalWorld world, Dynamic<T> in, DynamicRegistryManager.Impl registry) {
		System.out.println("Adding");
		Dynamic<T> dimensions = in.get("dimensions").orElseEmptyMap();
		if (!dimensions.get(world.worldId.toString()).result().isPresent()) {
			dimensions.set(world.worldId.toString(), new Dynamic<T>(dimensions.getOps(), DimensionOptions.CODEC.encodeStart(dimensions.getOps(), world.worldDimensionOptions.apply(registry.get(Registry.DIMENSION_TYPE_KEY), registry.get(Registry.BIOME_KEY), in.get("seed").asLong(0))).result().get()));
		}
		in.set("dimensions", dimensions);
		return in;
	}

}
