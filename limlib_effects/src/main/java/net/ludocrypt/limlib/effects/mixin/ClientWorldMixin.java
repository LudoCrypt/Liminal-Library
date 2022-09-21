package net.ludocrypt.limlib.effects.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.effects.render.sky.SkyEffects;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Holder;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

	@Inject(method = "Lnet/minecraft/client/world/ClientWorld;<init>(Lnet/minecraft/client/network/ClientPlayNetworkHandler;Lnet/minecraft/client/world/ClientWorld$Properties;Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/Holder;IILjava/util/function/Supplier;Lnet/minecraft/client/render/WorldRenderer;ZJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/SkyProperties;byDimensionType(Lnet/minecraft/world/dimension/DimensionType;)Lnet/minecraft/client/render/SkyProperties;", shift = Shift.BEFORE))
	private void limlib$init(ClientPlayNetworkHandler clientPlayNetworkHandler, ClientWorld.Properties properties, RegistryKey<World> registryKey, Holder<DimensionType> holder, int i, int j, Supplier<Profiler> supplier, WorldRenderer worldRenderer, boolean bl, long l, CallbackInfo ci) {
		SkyEffects.tempHolder = holder;
	}

}
