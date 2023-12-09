package net.ludocrypt.limlib.impl.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.ludocrypt.limlib.impl.access.TagGroupLoaderAccess;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.resource.ResourceManager;

@Mixin(TagManagerLoader.class)
public class TagManagerLoaderMixin {

	@Inject(method = "Lnet/minecraft/registry/tag/TagManagerLoader;buildGroup(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;Lnet/minecraft/registry/DynamicRegistryManager$RegistryEntry;)Ljava/util/concurrent/CompletableFuture;", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private <T> void limlib$buildGroup(ResourceManager resourceManager, Executor prepareExecutor,
			DynamicRegistryManager.RegistryEntry<T> registryEntry,
			CallbackInfoReturnable<CompletableFuture<TagManagerLoader.LoadResult<T>>> ci,
			RegistryKey<? extends Registry<T>> registryKey, Registry<T> registry, TagGroupLoader<Holder<T>> tagGroupLoader) {
		((TagGroupLoaderAccess) tagGroupLoader).setRegistryKey(registryKey);
	}

}
