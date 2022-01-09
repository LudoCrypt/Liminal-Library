package net.ludocrypt.limlib.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.ludocrypt.limlib.impl.world.LiminalDimensions;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;

@Mixin(DimensionOptions.class)
public class DimensionOptionsMixin {

	@Shadow
	@Final
	private static Set<RegistryKey<DimensionOptions>> BASE_DIMENSIONS;

	static {
		LiminalDimensions.LIMINAL_WORLD_REGISTRY.forEach((world) -> BASE_DIMENSIONS.add(world.worldDimensionOptionsRegistryKey));
	}

}
