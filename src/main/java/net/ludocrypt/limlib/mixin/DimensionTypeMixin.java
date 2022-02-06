package net.ludocrypt.limlib.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;

import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.impl.LiminalEffects;
import net.ludocrypt.limlib.impl.world.LiminalDimensions;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

@Mixin(DimensionType.class)
public class DimensionTypeMixin implements DimensionTypeAccess {

	@Shadow
	@Mutable
	@Final
	public static Codec<DimensionType> CODEC;

	private LiminalEffects liminalEffects = new LiminalEffects(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

	@Inject(method = "addRegistryDefaults", at = @At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$addRegistryDefaults(DynamicRegistryManager registryManager, CallbackInfoReturnable<DynamicRegistryManager> ci, MutableRegistry<DimensionType> mutableRegistry) {
		LiminalDimensions.LIMINAL_WORLD_REGISTRY.forEach((world) -> mutableRegistry.add(world.worldDimensionTypeRegistryKey, world.worldDimensionType, Lifecycle.stable()));
	}

	@Inject(method = "Lnet/minecraft/world/dimension/DimensionType;createDefaultDimensionOptions(Lnet/minecraft/util/registry/DynamicRegistryManager;JZ)Lnet/minecraft/util/registry/SimpleRegistry;", at = @At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$createDefaultDimensionOptions(DynamicRegistryManager registryManager, long seed, boolean bl, CallbackInfoReturnable<SimpleRegistry<DimensionOptions>> ci, SimpleRegistry<DimensionOptions> simpleRegistry, Registry<DimensionType> dimensionRegistry, Registry<Biome> biomeRegistry) {
		LiminalDimensions.LIMINAL_WORLD_REGISTRY.forEach((world) -> simpleRegistry.add(world.worldDimensionOptionsRegistryKey, world.worldDimensionOptions.apply(dimensionRegistry, biomeRegistry, seed), Lifecycle.stable()));
	}

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void clinit(CallbackInfo ci) {

		final Codec<DimensionType> in = CODEC;
		CODEC = new Codec<DimensionType>() {

			@Override
			public <T> DataResult<T> encode(DimensionType input, DynamicOps<T> ops, T prefix) {
				DataResult<T> encoded = in.encode(input, ops, prefix);
				if (encoded.result().isPresent()) {
					if (encoded.result().get()instanceof JsonObject json) {
						json.add("limlib_liminal_effects", LiminalEffects.CODEC.encodeStart(JsonOps.INSTANCE, ((DimensionTypeAccess) input).getLiminalEffects()).result().get());
					}
				}
				return encoded;
			}

			@Override
			public <T> DataResult<Pair<DimensionType, T>> decode(DynamicOps<T> ops, T input) {
				DataResult<Pair<DimensionType, T>> decoded = in.decode(ops, input);
				if (decoded.result().isPresent() && input instanceof JsonObject json) {
					DimensionType dimensionType = decoded.result().get().getFirst();
					if (json.has("limlib_liminal_effects")) {
						((DimensionTypeAccess) dimensionType).setLiminalEffects(LiminalEffects.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(json.get("limlib_liminal_effects").toString())).result().get());
					}
				}
				return decoded;
			}

		};
	}

	@Override
	public LiminalEffects getLiminalEffects() {
		return liminalEffects;
	}

	@Override
	public void setLiminalEffects(LiminalEffects effects) {
		this.liminalEffects = effects;
	}

}
