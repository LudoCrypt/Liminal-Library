package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.impl.render.LiminalDimensionEffects;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {

	@Shadow
	@Final
	private static Object2ObjectMap<Identifier, DimensionEffects> BY_IDENTIFIER;

	static {
		LiminalDimensionEffects.DIMENSION_EFFECTS_REGISTRY.forEach((key, effects) -> BY_IDENTIFIER.put(key.getValue(), effects));
	}

}
