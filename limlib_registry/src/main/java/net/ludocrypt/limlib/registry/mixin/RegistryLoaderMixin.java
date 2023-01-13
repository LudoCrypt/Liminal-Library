package net.ludocrypt.limlib.registry.mixin;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Lifecycle;

import net.ludocrypt.limlib.registry.registration.LimlibWorld;
import net.ludocrypt.limlib.registry.registration.LimlibWorld.RegistryProvider;
import net.ludocrypt.limlib.registry.registration.RegistryLoaderBootstrap;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.HolderProvider;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.ResourceFileNamespace;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

	@Inject(method = "Lnet/minecraft/registry/RegistryLoader;loadRegistryContents(Lnet/minecraft/registry/RegistryOps$RegistryInfoLookup;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private static <E> void limlib$loadRegistryContents(RegistryOps.RegistryInfoLookup infoLookup, ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> registry, Decoder<E> decoder, Map<RegistryKey<?>, Exception> readFailures, CallbackInfo ci, String string, ResourceFileNamespace resourceFileNamespace, RegistryOps<JsonElement> registryOps, Iterator<Map.Entry<Identifier, Resource>> var9, Map.Entry<Identifier, Resource> entry, Identifier identifier, RegistryKey<E> registryKey2, Resource resource, Reader reader, JsonElement jsonElement) {
		if (registryKey2.isOf(RegistryKeys.GENERATOR_TYPE)) {
			JsonObject presetType = jsonElement.getAsJsonObject();
			JsonObject dimensions = presetType.get("dimensions").getAsJsonObject();
			LimlibWorld.LIMLIB_WORLD.getEntries().forEach((world) -> dimensions.add(world.getKey().getValue().toString(), DimensionOptions.CODEC.encodeStart(registryOps, world.getValue().getDimensionOptionsSupplier().apply(new RegistryProvider() {

				@Override
				public <T> HolderProvider<T> get(RegistryKey<Registry<T>> key) {
					return registryOps.getHolderProvider(key).get();
				}

			})).result().get()));
		}
	}

	@SuppressWarnings("unchecked")
	@Inject(method = "Lnet/minecraft/registry/RegistryLoader;loadRegistryContents(Lnet/minecraft/registry/RegistryOps$RegistryInfoLookup;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/ResourceFileNamespace;findMatchingResources(Lnet/minecraft/resource/ResourceManager;)Ljava/util/Map;", shift = Shift.BEFORE))
	private static <E> void limlib$loadRegistryContents(RegistryOps.RegistryInfoLookup infoLookup, ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> registry, Decoder<E> decoder, Map<RegistryKey<?>, Exception> readFailures, CallbackInfo ci) {
		QuiltLoader.getEntrypoints(RegistryLoaderBootstrap.ENTRYPOINT_KEY, RegistryLoaderBootstrap.class).forEach((bootstrap) -> bootstrap.register(infoLookup, registryKey, registry));
		if (registryKey.equals(RegistryKeys.DIMENSION_TYPE)) {
			LimlibWorld.LIMLIB_WORLD.getEntries().forEach((world) -> ((MutableRegistry<DimensionType>) registry).register(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, world.getKey().getValue()), world.getValue().getDimensionTypeSupplier().get(), Lifecycle.stable()));
		}
	}

	@Inject(method = "Lnet/minecraft/registry/RegistryLoader;loadRegistriesIntoManager(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Frozen;", at = @At("TAIL"))
	private static void limlib$loadRegistriesIntoManager(ResourceManager resourceManager, DynamicRegistryManager registryManager, List<RegistryLoader.DecodingData<?>> decodingData, CallbackInfoReturnable<DynamicRegistryManager.Frozen> ci) {
		LimlibWorld.LOADED_REGISTRY.set(registryManager.freeze());
	}

}
