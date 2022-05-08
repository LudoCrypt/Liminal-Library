package net.ludocrypt.limlib.mixin;

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

import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.ludocrypt.limlib.api.LiminalEffects;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Mixin(DimensionType.class)
public class DimensionTypeMixin implements DimensionEffectsAccess {

	@Shadow
	@Mutable
	@Final
	public static Codec<DimensionType> CODEC;

	private LiminalEffects liminalEffects = new LiminalEffects();

	@Inject(method = "Lnet/minecraft/world/dimension/DimensionType;addRegistryDefaults(Lnet/minecraft/util/registry/DynamicRegistryManager$Mutable;)Lnet/minecraft/util/registry/DynamicRegistryManager$Mutable;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$addRegistryDefaults(DynamicRegistryManager.Mutable registryManager, CallbackInfoReturnable<DynamicRegistryManager.Mutable> ci, MutableRegistry<DimensionType> mutableRegistry) {
		LimlibRegistries.LIMINAL_WORLD.forEach((world) -> mutableRegistry.add(world.getDimensionTypeKey(), world.getDimensionType(), Lifecycle.stable()));
	}

	@Inject(method = "Lnet/minecraft/world/dimension/DimensionType;createDefaultDimensionOptions(Lnet/minecraft/util/registry/DynamicRegistryManager;JZ)Lnet/minecraft/util/registry/Registry;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$createDefaultDimensionOptions(DynamicRegistryManager registryManager, long seed, boolean bl, CallbackInfoReturnable<Registry<DimensionOptions>> ci, MutableRegistry<DimensionOptions> dimensionOptionsRegistry, Registry<DimensionType> dimensionTypeRegistry, Registry<Biome> biomeRegistry, Registry<StructureSet> stuctureSetRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseSettingsRegistry) {
		LimlibRegistries.LIMINAL_WORLD.forEach((world) -> dimensionOptionsRegistry.add(world.getDimensionKey(), world.getDimensionOptionsGetter().get(world, dimensionTypeRegistry, biomeRegistry, stuctureSetRegistry, chunkGeneratorSettingsRegistry, noiseSettingsRegistry, registryManager, seed), Lifecycle.stable()));
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
						json.add("limlib_liminal_effects", LiminalEffects.CODEC.encodeStart(JsonOps.INSTANCE, ((DimensionEffectsAccess) input).getLiminalEffects()).result().get());
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
						((DimensionEffectsAccess) dimensionType).setLiminalEffects(LiminalEffects.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(json.get("limlib_liminal_effects").toString())).result().get());
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
