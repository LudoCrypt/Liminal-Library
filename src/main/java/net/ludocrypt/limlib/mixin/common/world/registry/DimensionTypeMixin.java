package net.ludocrypt.limlib.mixin.common.world.registry;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.api.LiminalEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.dimension.DimensionType;

@Mixin(DimensionType.class)
public class DimensionTypeMixin implements DimensionTypeAccess {

	@Shadow
	@Mutable
	@Final
	public static Codec<DimensionType> CODEC;

	private LiminalEffects liminalEffects = new LiminalEffects();

	@Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/dynamic/RegistryElementCodec;of(Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lnet/minecraft/util/dynamic/RegistryElementCodec;", shift = Shift.BEFORE))
	private static void limlib$clinit(CallbackInfo ci) {

		final Codec<DimensionType> in = CODEC;
		CODEC = new Codec<DimensionType>() {
			@Override
			public <T> DataResult<T> encode(DimensionType input, DynamicOps<T> ops, T prefix) {
				DataResult<T> encoded = in.encode(input, ops, prefix);
				if (encoded.result().isPresent()) {
					if (encoded.result().get()instanceof NbtCompound nbt) {
						nbt.put("limlib_liminal_effects", LiminalEffects.CODEC.encodeStart(NbtOps.INSTANCE, ((DimensionTypeAccess) (Object) input).getLiminalEffects()).result().get());
					} else if (encoded.result().get()instanceof JsonObject json) {
						json.add("limlib_liminal_effects", LiminalEffects.CODEC.encodeStart(JsonOps.INSTANCE, ((DimensionTypeAccess) (Object) input).getLiminalEffects()).result().get());
					}
				}
				return encoded;
			}

			@Override
			public <T> DataResult<Pair<DimensionType, T>> decode(DynamicOps<T> ops, T input) {
				DataResult<Pair<DimensionType, T>> decoded = in.decode(ops, input);
				if (decoded.result().isPresent()) {
					DimensionType dimensionType = decoded.result().get().getFirst();
					if (input instanceof NbtCompound nbt) {
						if (nbt.contains("limlib_liminal_effects", 10)) {
							((DimensionTypeAccess) (Object) dimensionType).setLiminalEffects(LiminalEffects.CODEC.parse(NbtOps.INSTANCE, nbt.get("limlib_liminal_effects")).result().get());
						}
					} else if (input instanceof JsonObject json) {
						if (json.has("limlib_liminal_effects")) {
							((DimensionTypeAccess) (Object) dimensionType).setLiminalEffects(LiminalEffects.CODEC.parse(JsonOps.INSTANCE, json.get("limlib_liminal_effects")).result().get());
						}
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
