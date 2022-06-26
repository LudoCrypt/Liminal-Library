package net.ludocrypt.limlib.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.ludocrypt.limlib.access.WorldRendererAccess;
import net.ludocrypt.limlib.api.render.LiminalSkyRenderer;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
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

	@Override
	public void renderSky(MatrixStack matrices, Matrix4f positionMatrix, float tickDelta) {
		Optional<LiminalSkyRenderer> sky = ((DimensionEffectsAccess) (Object) this.world.getDimension()).getLiminalEffects().getSky();
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

		Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix().copy();

		SimpleFramebuffer frameBuffer = new SimpleFramebuffer(client.getFramebuffer().viewportWidth, client.getFramebuffer().viewportHeight, false, false);

		// Render Hands
		if (((GameRendererAccessor) client.gameRenderer).isRenderHand()) {
			frameBuffer.beginWrite(true);

			this.isRenderingHands = true;
			matrices.push();

			matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
			matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(camera.getYaw()));
			matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(camera.getPitch()));

			((GameRendererAccessorTwo) client.gameRenderer).callRenderHand(matrices, camera, tickDelta);

			matrices.pop();

			this.isRenderingHands = false;

			frameBuffer.endWrite();
			frameBuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
			client.getFramebuffer().beginWrite(true);
		}

		LimlibRegistries.LIMINAL_QUAD_RENDERER.forEach((renderer) -> {
			renderer.heldItemRenderQueue.forEach(Runnable::run);
			renderer.heldItemRenderQueue.clear();
		});

		// Reset Projection matrix
		RenderSystem.setProjectionMatrix(projectionMatrix);

		// Render Entities
		frameBuffer.beginWrite(true);

		isRenderingItems = true;

		for (Entity entity : this.world.getEntities()) {
			if (entity instanceof ItemEntity) {
				this.renderEntity(entity, camera.getPos().getX(), camera.getPos().getY(), camera.getPos().getZ(), tickDelta, matrices, this.bufferBuilders.getEntityVertexConsumers());
			}
		}

		isRenderingItems = false;

		frameBuffer.endWrite();
		frameBuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
		client.getFramebuffer().beginWrite(true);

		LimlibRegistries.LIMINAL_QUAD_RENDERER.forEach((renderer) -> {
			renderer.itemRenderQueue.forEach(Runnable::run);
			renderer.itemRenderQueue.clear();
		});

		// Render Blocks
		this.getQuadRenderData().forEach((pair) -> {
			BlockPos pos = pair.getFirst();
			BlockState state = pair.getSecond();
			matrices.push();
			matrices.translate(pos.getX() - camera.getPos().getX(), pos.getY() - camera.getPos().getY(), pos.getZ() - camera.getPos().getZ());

			((BakedModelAccess) MinecraftClient.getInstance().getBlockRenderManager().getModel(state)).getSubQuads().forEach((id, quads) -> LimlibRegistries.LIMINAL_QUAD_RENDERER.get(id).renderQuads(quads, world, pos, state, matrices, camera));

			matrices.pop();
		});

		modelViewStack.pop();
		RenderSystem.applyModelViewMatrix();

		frameBuffer.delete();
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
