package net.ludocrypt.limlib.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.limlib.impl.render.LiminalSkyRendering;
import net.ludocrypt.limlib.impl.sound.LiminalTravelSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private ClientWorld world;

	@Inject(method = "processWorldEvent", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$processWorldEvent(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo ci, Random random) {
		switch (eventId) {
		case 29848748:
			this.client.getSoundManager().play(PositionedSoundInstance.ambient(LiminalTravelSounds.getCurrent(this.world.getRegistryKey()), random.nextFloat() * 0.4F + 0.8F, 0.25F));
			break;
		}
	}

	@Inject(method = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLjava/lang/Runnable;)V", at = @At("HEAD"))
	private void limlib$render$clear(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Runnable runnable, CallbackInfo ci) {
		if (!FabricLoader.getInstance().isModLoaded("iris")) {
			LiminalSkyRendering.getCurrent(this.world.getRegistryKey()).renderSky((WorldRenderer) (Object) this, client, matrices, projectionMatrix, tickDelta);
		}
	}

	@Inject(method = "render", at = @At(value = "RETURN", shift = Shift.BEFORE))
	private void limlib$render$return(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
		if (FabricLoader.getInstance().isModLoaded("iris")) {
			LiminalSkyRendering.getCurrent(this.world.getRegistryKey()).renderSky((WorldRenderer) (Object) this, client, matrices, positionMatrix, tickDelta);
		}
	}

}
