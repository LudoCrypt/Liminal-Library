package net.ludocrypt.limlib.render.mixin.render;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.framebuffer.SimpleFramebuffer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.ludocrypt.limlib.render.LimlibRender;
import net.ludocrypt.limlib.render.access.ChunkBuilderAccess;
import net.ludocrypt.limlib.render.access.RenderMapAccess;
import net.ludocrypt.limlib.render.access.WorldRendererAccess;
import net.ludocrypt.limlib.render.access.sodium.SodiumWorldRendererAccess;
import net.ludocrypt.limlib.render.mixin.render.gui.GameRendererAccessor;
import net.ludocrypt.limlib.render.mixin.render.gui.GameRendererAccessorTwo;
import net.ludocrypt.limlib.render.util.RenderSetup;
import net.ludocrypt.limlib.render.util.skybox.Skybox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
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
	private ChunkBuilder chunkBuilder;

	@Shadow
	@Final
	private ObjectArrayList<WorldRenderer.ChunkInfo> chunkInfoList;

	@Unique
	private boolean isRenderingHands = false;

	@Unique
	private boolean isRenderingItems = false;

	@Override
	public void render(MatrixStack matrices, Matrix4f positionMatrix, float tickDelta, Camera camera) {
		this.renderSkyboxes(matrices, positionMatrix, tickDelta);
		this.renderBlocks(matrices, positionMatrix);

		MatrixStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.push();
		modelViewStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
		modelViewStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw()));
		modelViewStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
		RenderSystem.applyModelViewMatrix();

		SimpleFramebuffer frameBuffer = new SimpleFramebuffer(client.getFramebuffer().viewportWidth, client.getFramebuffer().viewportHeight, false, false);

		this.renderHands(frameBuffer, tickDelta, matrices, camera);
		this.renderItems(frameBuffer, tickDelta, matrices, camera);

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

		LimlibRender.HAND_RENDER_QUEUE.forEach(Runnable::run);
		LimlibRender.HAND_RENDER_QUEUE.clear();

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

		LimlibRender.ITEM_RENDER_QUEUE.forEach(Runnable::run);
		LimlibRender.ITEM_RENDER_QUEUE.clear();
	}

	@Override
	public void renderSkyboxes(MatrixStack matrices, Matrix4f positionMatrix, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();

		Optional<Skybox> sky = Skybox.SKYBOX.getOrEmpty(client.world.getRegistryKey().getValue());
		if (sky.isPresent()) {
			sky.get().renderSky(((WorldRenderer) (Object) this), client, matrices, positionMatrix, tickDelta);
		}
	}

	@Override
	public void renderBlocks(MatrixStack matrices, Matrix4f positionMatrix) {
		if (!QuiltLoader.isModLoaded("sodium")) {
			((ChunkBuilderAccess) chunkBuilder).uploadRenderMap();
			for (WorldRenderer.ChunkInfo chunkInfo : this.chunkInfoList) {
				Map<BakedQuad, Pair<VertexBuffer, Identifier>> renderBufferMap = ((RenderMapAccess) chunkInfo.chunk.getData()).getShaderBuffers();
				renderBufferMap.forEach((quad, renderPair) -> this.renderBuffer(matrices, positionMatrix, renderPair.getSecond(), renderPair.getFirst(), quad, chunkInfo.chunk.getOrigin()));
			}
		} else {
			((SodiumWorldRendererAccess) (Object) this).renderSodiumBlocks(matrices, positionMatrix);
		}
	}

	@Override
	public void renderBuffer(MatrixStack matrices, Matrix4f positionMatrix, Identifier shaderId, VertexBuffer buffer, BakedQuad quad, BlockPos origin) {
		ShaderProgram shader = LimlibRender.LOADED_SHADERS.get(shaderId);
		if (shader != null) {
			RenderSystem.depthMask(true);
			RenderSystem.enableBlend();
			RenderSystem.enableDepthTest();
			RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
			RenderSystem.polygonOffset(3.0F, 3.0F);
			RenderSystem.enablePolygonOffset();
			RenderSystem.setShader(() -> shader);
			RenderSystem.setShaderTexture(0, new Identifier(quad.getSprite().getId().getNamespace(), "textures/" + quad.getSprite().getId().getPath() + ".png"));
			client.gameRenderer.getLightmapTextureManager().enable();

			buffer.bind();

			List<RenderSetup> renderSetup = LimlibRender.SETUP_RENDERER_REGISTRY.get(shaderId);
			if (renderSetup != null) {
				renderSetup.forEach((setup) -> setup.setup(matrices, shaderId, quad));
			}

			if (origin != null) {
				if (shader.chunkOffset != null) {
					BlockPos blockPos = origin;
					Camera camera = client.gameRenderer.getCamera();
					float vx = (float) (blockPos.getX() - camera.getPos().getX());
					float vy = (float) (blockPos.getY() - camera.getPos().getY());
					float vz = (float) (blockPos.getZ() - camera.getPos().getZ());
					shader.chunkOffset.setVec3(vx, vy, vz);
				}
			}

			if (shader.getUniform("FullUV") != null) {
				shader.getUniform("FullUV").setVec4(quad.getSprite().getMinU(), quad.getSprite().getMinV(), quad.getSprite().getMaxU(), quad.getSprite().getMaxV());
			}

			if (shader.getUniform("renderAsEntity") != null) {
				shader.getUniform("renderAsEntity").setFloat(0.0F);
			}

			buffer.setShader(matrices.peek().getPosition(), positionMatrix, shader);

			VertexBuffer.unbind();

			client.gameRenderer.getLightmapTextureManager().disable();

			RenderSystem.polygonOffset(0.0F, 0.0F);
			RenderSystem.disablePolygonOffset();
			RenderSystem.disableBlend();
		}
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
