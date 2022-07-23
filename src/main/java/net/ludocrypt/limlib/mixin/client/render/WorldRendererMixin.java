package net.ludocrypt.limlib.mixin.client.render;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.ludocrypt.limlib.access.WorldRendererAccess;
import net.ludocrypt.limlib.api.LiminalEffects;
import net.ludocrypt.limlib.api.render.LiminalSkyRenderer;
import net.ludocrypt.limlib.impl.LimlibRendering;
import net.ludocrypt.limlib.mixin.client.render.chunk.ChunkInfoAccessor;
import net.ludocrypt.limlib.mixin.client.render.gui.GameRendererAccessor;
import net.ludocrypt.limlib.mixin.client.render.gui.GameRendererAccessorTwo;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.WorldRenderer.ChunkInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Mixin(value = WorldRenderer.class, priority = 900)
public abstract class WorldRendererMixin implements WorldRendererAccess {

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

	@Unique
	private boolean isRenderingHands = false;

	@Unique
	private boolean isRenderingItems = false;

	@ModifyVariable(method = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
	private double limlib$renderSky(double in) {

		LiminalEffects effects = ((DimensionTypeAccess) (Object) world.getDimension()).getLiminalEffects();

		if (effects.getSkyShading().isPresent()) {
			return effects.getSkyShading().get();
		}

		return in;
	}

	@Override
	public void renderSky(MatrixStack matrices, Matrix4f positionMatrix, float tickDelta) {
		Optional<LiminalSkyRenderer> sky = ((DimensionTypeAccess) (Object) this.world.getDimension()).getLiminalEffects().getSky();
		if (sky.isPresent()) {
			sky.get().renderSky((WorldRenderer) (Object) this, client, matrices, positionMatrix, tickDelta);
		}
	}

	@Override
	public void renderQuads(float tickDelta, MatrixStack matrices, Camera camera) {
		MatrixStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.push();
		modelViewStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
		modelViewStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw()));
		modelViewStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
		RenderSystem.applyModelViewMatrix();

		SimpleFramebuffer frameBuffer = new SimpleFramebuffer(client.getFramebuffer().viewportWidth, client.getFramebuffer().viewportHeight, false, false);

		this.renderHands(frameBuffer, tickDelta, matrices, camera);
		this.renderItems(frameBuffer, tickDelta, matrices, camera);
		this.renderBlocks(frameBuffer, tickDelta, matrices, camera);

		modelViewStack.pop();
		RenderSystem.applyModelViewMatrix();

		frameBuffer.delete();
		client.getFramebuffer().beginWrite(true);
	}

	@Override
	public void renderHands(Framebuffer framebuffer, float tickDelta, MatrixStack matrices, Camera camera) {
		Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix().copy();

		if (((GameRendererAccessor) client.gameRenderer).isRenderHand()) {
			framebuffer.beginWrite(true);

			this.isRenderingHands = true;
			matrices.push();

			matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
			matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(camera.getYaw()));
			matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(camera.getPitch()));

			((GameRendererAccessorTwo) client.gameRenderer).callRenderHand(matrices, camera, tickDelta);

			matrices.pop();

			this.isRenderingHands = false;

			framebuffer.endWrite();
			client.getFramebuffer().beginWrite(true);
		}

		LimlibRendering.LIMINAL_QUAD_RENDERER.forEach((renderer) -> {
			renderer.heldItemRenderQueue.forEach(Runnable::run);
			renderer.heldItemRenderQueue.clear();
		});

		RenderSystem.setProjectionMatrix(projectionMatrix);
	}

	@Override
	public void renderItems(Framebuffer framebuffer, float tickDelta, MatrixStack matrices, Camera camera) {
		framebuffer.beginWrite(true);

		isRenderingItems = true;
		for (Entity entity : this.world.getEntities()) {
			if (entity instanceof ItemEntity || entity instanceof FoxEntity || entity instanceof ItemFrameEntity || entity instanceof PlayerEntity) {
				if (!(entity instanceof PlayerEntity && client.player == entity && !client.gameRenderer.getCamera().isThirdPerson())) {
					this.renderEntity(entity, camera.getPos().getX(), camera.getPos().getY(), camera.getPos().getZ(), tickDelta, matrices, this.bufferBuilders.getEntityVertexConsumers());
				}
			}
		}
		isRenderingItems = false;

		this.bufferBuilders.getEntityVertexConsumers().draw();

		framebuffer.endWrite();
		client.getFramebuffer().beginWrite(true);

		LimlibRendering.LIMINAL_QUAD_RENDERER.forEach((renderer) -> {
			renderer.itemRenderQueue.forEach(Runnable::run);
			renderer.itemRenderQueue.clear();
		});
	}

	@Override
	public void renderBlocks(Framebuffer framebuffer, float tickDelta, MatrixStack matrices, Camera camera) {
		this.getQuadRenderData().forEach((pair) -> {
			BlockPos pos = pair.getFirst();
			BlockState state = pair.getSecond();
			matrices.push();
			matrices.translate(pos.getX() - camera.getPos().getX(), pos.getY() - camera.getPos().getY(), pos.getZ() - camera.getPos().getZ());

			((BakedModelAccess) MinecraftClient.getInstance().getBlockRenderManager().getModel(state)).getSubQuads().forEach((id, quads) -> LimlibRendering.LIMINAL_QUAD_RENDERER.get(id).renderQuads(quads, world, pos, state, matrices, camera));

			matrices.pop();
		});
	}

	@Override
	public List<Pair<BlockPos, BlockState>> getQuadRenderData() {
		List<Pair<BlockPos, BlockState>> list = Lists.newArrayList();

		if (FabricLoader.getInstance().isModLoaded("sodium")) {
			((RenderDataAccess) this).getCustomQuadData().forEach((pos, state) -> list.add(Pair.of(pos, state)));
		} else {
			this.chunkInfos.forEach((info) -> ((RenderDataAccess) ((ChunkInfoAccessor) info).getChunk()).getCustomQuadData().forEach((pos, state) -> list.add(Pair.of(pos, state))));
		}

		return list;
	}

	@Override
	public boolean isRenderingHands() {
		return isRenderingHands;
	}

	@Override
	public boolean isRenderingItems() {
		return isRenderingItems;
	}

	@Shadow
	abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

}
