package net.ludocrypt.limlib.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.api.render.SkyHook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private ClientWorld world;

	@Inject(method = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V", at = @At("HEAD"))
	private void limlib$render$clear(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Runnable runnable, CallbackInfo ci) {
		if (!FabricLoader.getInstance().isModLoaded("iris")) {
			Optional<SkyHook> sky = ((DimensionTypeAccess) this.world.getDimension()).getLiminalEffects().getSky();
			if (sky.isPresent()) {
				sky.get().renderSky((WorldRenderer) (Object) this, client, matrices, projectionMatrix, tickDelta);
			}
		}
	}

	@Inject(method = "render", at = @At(value = "RETURN", shift = Shift.BEFORE))
	private void limlib$render$return(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
		if (FabricLoader.getInstance().isModLoaded("iris")) {
			Optional<SkyHook> sky = ((DimensionTypeAccess) this.world.getDimension()).getLiminalEffects().getSky();
			if (sky.isPresent()) {
				sky.get().renderSky((WorldRenderer) (Object) this, client, matrices, positionMatrix, tickDelta);
			}
		}
	}

}
