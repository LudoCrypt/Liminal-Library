package net.ludocrypt.limlib.render.mixin.render;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.framebuffer.SimpleFramebuffer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.ludocrypt.limlib.render.LimlibRender;
import net.ludocrypt.limlib.render.access.BakedModelAccess;
import net.ludocrypt.limlib.render.access.BuiltChunkAccess;
import net.ludocrypt.limlib.render.access.WorldRendererAccess;
import net.ludocrypt.limlib.render.compat.SodiumBridge;
import net.ludocrypt.limlib.render.mixin.render.gui.GameRendererAccessor;
import net.ludocrypt.limlib.render.mixin.render.gui.GameRendererAccessorTwo;
import net.ludocrypt.limlib.render.skybox.Skybox;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.model.BakedModel;
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
import net.minecraft.util.random.RandomGenerator;

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
		if (!SodiumBridge.SODIUM_LOADED) {
			ObjectListIterator<WorldRenderer.ChunkInfo> chunkInfos = this.chunkInfoList.listIterator(this.chunkInfoList.size());

			while (chunkInfos.hasPrevious()) {
				WorldRenderer.ChunkInfo chunkInfo = chunkInfos.previous();
				ChunkBuilder.BuiltChunk builtChunk = chunkInfo.chunk;
				((BuiltChunkAccess) builtChunk).getSpecialModelBuffers().forEach((modelRenderer, vertexBuffer) -> renderBuffer(matrices, positionMatrix, modelRenderer, vertexBuffer, builtChunk.getOrigin()));
			}
		} else {
			Iterable<BlockPos> iterable = BlockPos.iterateOutwards(client.player.getBlockPos(), 10, 10, 10);
			List<BlockState> dontCheckStates = Lists.newArrayList();

			for (BlockPos pos : iterable) {
				BlockState state = world.getBlockState(pos);
				if (!dontCheckStates.contains(state)) {
					List<Pair<SpecialModelRenderer, BakedModel>> models = ((BakedModelAccess) client.getBakedModelManager().getBlockModels().getModel(state)).getModels(state);
					if (!models.isEmpty()) {

						Camera camera = client.gameRenderer.getCamera();
						matrices.push();
						matrices.translate(pos.getX() - camera.getPos().getX(), pos.getY() - camera.getPos().getY(), pos.getZ() - camera.getPos().getZ());

						MatrixStack matrix = new MatrixStack();
						matrix.multiplyMatrix(matrices.peek().getPosition().copy());

						RenderSystem.disableTexture();
						RenderSystem.depthMask(true);
						RenderSystem.enableBlend();
						RenderSystem.enableDepthTest();
						RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
						RenderSystem.polygonOffset(3.0F, 3.0F);
						RenderSystem.enablePolygonOffset();
						client.gameRenderer.getLightmapTextureManager().enable();

						models.forEach((renderPair) -> {
							SpecialModelRenderer modelRenderer = renderPair.getFirst();
							BakedModel model = renderPair.getSecond();

							ShaderProgram shader = LimlibRender.LOADED_SHADERS.get(modelRenderer);
							RenderSystem.setShader(() -> shader);
							modelRenderer.setup(matrix, shader);

							if (shader.getUniform("renderAsEntity") != null) {
								shader.getUniform("renderAsEntity").setFloat(0.0F);
							}

							BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();
							bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);

							RandomGenerator random = RandomGenerator.createLegacy(state.getRenderingSeed(pos));

							client.getBlockRenderManager().getModelRenderer().render(world, model, state, pos, matrix, bufferBuilder, true, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);

							BufferRenderer.drawWithShader(bufferBuilder.end());
						});

						client.gameRenderer.getLightmapTextureManager().disable();
						RenderSystem.polygonOffset(0.0F, 0.0F);
						RenderSystem.disablePolygonOffset();
						RenderSystem.disableBlend();
						RenderSystem.enableTexture();

						matrices.pop();
					} else {
						dontCheckStates.add(state);
					}
				}
			}
		}
	}

	@Override
	public void renderBuffer(MatrixStack matrices, Matrix4f positionMatrix, SpecialModelRenderer modelRenderer, VertexBuffer vertexBuffer, BlockPos origin) {
		ShaderProgram shader = LimlibRender.LOADED_SHADERS.get(modelRenderer);
		if (shader != null && ((VertexBufferAccessor) vertexBuffer).getIndexCount() > 0) {
			RenderSystem.depthMask(true);
			RenderSystem.enableBlend();
			RenderSystem.enableDepthTest();
			RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
			RenderSystem.polygonOffset(3.0F, 3.0F);
			RenderSystem.enablePolygonOffset();
			RenderSystem.setShader(() -> shader);
			client.gameRenderer.getLightmapTextureManager().enable();

			vertexBuffer.bind();

			modelRenderer.setup(matrices, shader);
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

			if (shader.getUniform("renderAsEntity") != null) {
				shader.getUniform("renderAsEntity").setFloat(0.0F);
			}

			vertexBuffer.setShader(matrices.peek().getPosition(), positionMatrix, shader);

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
