package net.ludocrypt.limlib.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.ludocrypt.limlib.api.render.LiminalSkyRenderer;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.WorldRenderer.ChunkInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Mixin(value = WorldRenderer.class, priority = 1200)
public abstract class WorldRendererMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private ClientWorld world;

	@Shadow
	@Final
	private BufferBuilderStorage bufferBuilders;

	@Shadow
	@Final
	private ObjectArrayList<ChunkInfo> chunkInfos;

	@Inject(method = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", shift = At.Shift.AFTER, remap = false))
	private void limlib$render$clear(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
		if (!FabricLoader.getInstance().isModLoaded("iris")) {
			this.limlib$renderQuads(matrices, camera);
		}
	}

	@Inject(method = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"))
	private void limlib$render$clear(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo ci) {
		if (!FabricLoader.getInstance().isModLoaded("iris")) {
			limlib$renderSky(matrices, projectionMatrix, tickDelta);
		}
	}

	@Inject(method = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", at = @At(value = "RETURN", shift = Shift.BEFORE))
	private void limlib$render$return(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
		if (FabricLoader.getInstance().isModLoaded("iris")) {
			limlib$renderSky(matrices, positionMatrix, tickDelta);

			MatrixStack matrixStack = RenderSystem.getModelViewStack();
			matrixStack.push();
			matrixStack.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
			RenderSystem.applyModelViewMatrix();
			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();

			matrixStack.push();
			matrixStack.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
			RenderSystem.applyModelViewMatrix();

//			this.renderWeather(lightmapTextureManager, tickDelta, camera.getPos().getX(), camera.getPos().getY(), camera.getPos().getZ());

			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			matrixStack.pop();
			RenderSystem.applyModelViewMatrix();

			this.limlib$renderQuads(matrices, camera);
		}
	}

	@Unique
	private void limlib$renderSky(MatrixStack matrices, Matrix4f positionMatrix, float tickDelta) {
		Optional<LiminalSkyRenderer> sky = ((DimensionEffectsAccess) (Object) this.world.getDimension()).getLiminalEffects().getSky();
		if (sky.isPresent()) {
			sky.get().renderSky((WorldRenderer) (Object) this, client, matrices, positionMatrix, tickDelta);
		}
	}

	@Unique
	private void limlib$renderQuads(MatrixStack matrices, Camera camera) {
		MatrixStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.push();
		modelViewStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
		modelViewStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw()));
		modelViewStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
		RenderSystem.applyModelViewMatrix();

		this.getQuadRenderData().forEach((pair) -> {
			BlockPos pos = pair.getFirst();
			BlockState state = pair.getSecond();
			matrices.push();
			matrices.translate(pos.getX() - camera.getPos().getX(), pos.getY() - camera.getPos().getY(), pos.getZ() - camera.getPos().getZ());

			((BakedModelAccess) MinecraftClient.getInstance().getBlockRenderManager().getModel(state)).getSubQuads().forEach((id, quads) -> LimlibRegistries.LIMINAL_QUAD_RENDERER.get(id).renderQuads(quads, world, Optional.of(pos), Either.left(state), matrices, camera, false));

			matrices.pop();
		});

		modelViewStack.pop();
		RenderSystem.applyModelViewMatrix();
	}

	@Unique
	private List<Pair<BlockPos, BlockState>> getQuadRenderData() {
		List<Pair<BlockPos, BlockState>> list = Lists.newArrayList();

		if (FabricLoader.getInstance().isModLoaded("sodium")) {
			((RenderDataAccess) this).getCustomQuadData().forEach((pos, state) -> list.add(Pair.of(pos, state)));
		} else {
			this.chunkInfos.forEach((info) -> ((RenderDataAccess) ((ChunkInfoAccessor) info).getChunk()).getCustomQuadData().forEach((pos, state) -> list.add(Pair.of(pos, state))));
		}

		return list;
	}

	@Shadow
	abstract void renderWeather(LightmapTextureManager manager, float f, double d, double e, double g);

}
