package net.ludocrypt.limlib.registry.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Sets;

import net.ludocrypt.limlib.registry.registration.LimlibWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.unmapped.C_oiwekzxo;
import net.minecraft.world.dimension.DimensionOptions;

@Mixin(C_oiwekzxo.class)
public class DimensionOptionsC_oiwekzxoMixin {

	@Shadow
	@Final
	@Mutable
	private static Set<RegistryKey<DimensionOptions>> f_txffzdje;

	@Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/Set;size()I", shift = Shift.BEFORE, ordinal = 0))
	private static void limlib$clinit(CallbackInfo ci) {
		Set<RegistryKey<DimensionOptions>> dimensions = Sets.newHashSet();
		dimensions.addAll(f_txffzdje);
		LimlibWorld.LIMLIB_WORLD.getEntries().forEach((entry) -> dimensions.add(RegistryKey.of(RegistryKeys.DIMENSION, entry.getKey().getValue())));
		f_txffzdje = dimensions;
	}

}
