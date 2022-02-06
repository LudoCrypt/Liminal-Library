package net.ludocrypt.limlib.mixin;

import java.util.Optional;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.datafixers.util.Function4;

import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.api.world.LevelStorageHacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClient.IntegratedResourceManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	public ClientPlayerEntity player;

	@Shadow
	public ClientWorld world;

	@Inject(method = "getMusicType", at = @At("HEAD"), cancellable = true)
	private void limlib$getMusicType(CallbackInfoReturnable<MusicSound> ci) {
		if (this.player != null) {
			Optional<MusicSound> optional = ((DimensionTypeAccess) this.world.getDimension()).getLiminalEffects().getMusic();
			if (optional.isPresent()) {
				ci.setReturnValue(optional.get());
			}
		}
	}

	@Inject(method = "createIntegratedResourceManager", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;get()Ljava/lang/Object;", shift = Shift.AFTER))
	private void limlib$createIntegratedResourceManager(DynamicRegistryManager.Impl registryManager, Function<LevelStorage.Session, DataPackSettings> dataPackSettingsGetter, Function4<LevelStorage.Session, DynamicRegistryManager.Impl, ResourceManager, DataPackSettings, SaveProperties> savePropertiesGetter, boolean safeMode, LevelStorage.Session storageSession, CallbackInfoReturnable<IntegratedResourceManager> ci) {
		if (LevelStorageHacks.earlyDynamicRegistryManager == null) {
			LevelStorageHacks.earlyDynamicRegistryManager = registryManager;
		}
	}

}
