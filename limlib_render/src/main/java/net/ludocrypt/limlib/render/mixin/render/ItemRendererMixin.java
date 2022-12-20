package net.ludocrypt.limlib.render.mixin.render;

import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.render.LimlibRender;
import net.ludocrypt.limlib.render.access.BakedModelAccess;
import net.ludocrypt.limlib.render.access.ItemRendererAccess;
import net.ludocrypt.limlib.render.access.WorldRendererAccess;
import net.ludocrypt.limlib.render.compat.IrisBridge;
import net.ludocrypt.limlib.render.special.SpecialModelRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.random.RandomGenerator;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin implements ItemRendererAccess {

	@Shadow
	@Final
	private ItemColors colors;

	@Unique
	private boolean inGui = false;

//	@Inject(method = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V", at = @At("HEAD"))
//	private void limlib$renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices, CallbackInfo ci) {
//		MinecraftClient client = MinecraftClient.getInstance();
//
//		boolean isHandRendering = ((WorldRendererAccess) client.worldRenderer).isRenderingHands() || (IrisBridge.IRIS_LOADED && IrisBridge.isHandRenderingActive());
//		boolean isItemRendering = ((WorldRendererAccess) client.worldRenderer).isRenderingItems();
//
//		if (isHandRendering || isItemRendering || inGui) {
//			ItemStack copiedStack = stack.copy();
//			Matrix3f matrixNormalClone = matrices.peek().getNormal().copy();
//			Matrix4f matrixPositionClone = matrices.peek().getPosition().copy();
//			List<Runnable> immediateRenderer = Lists.newArrayList();
//
//			(isHandRendering ? LimlibRender.HAND_RENDER_QUEUE : isItemRendering ? LimlibRender.ITEM_RENDER_QUEUE : immediateRenderer).add(() -> {
//				List<Pair<SpecialModelRenderer, BakedModel>> models = ((BakedModelAccess) model).getModels(null);
//				if (!models.isEmpty()) {
//					models.forEach((renderPair) -> {
//						ShaderProgram shader = LimlibRender.LOADED_SHADERS.get(SpecialModelRenderer.SPECIAL_MODEL_RENDERER.getKey(renderPair.getFirst()).get().getValue());
//						if (shader != null) {
//							RenderSystem.enableBlend();
//							RenderSystem.enableDepthTest();
//							RenderSystem.blendFuncSeparate(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA, GlStateManager.class_4535.ONE, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
//							RenderSystem.polygonOffset(3.0F, 3.0F);
//							RenderSystem.enablePolygonOffset();
//							RenderSystem.setShader(() -> shader);
//							client.gameRenderer.getLightmapTextureManager().enable();
//							client.gameRenderer.getOverlayTexture().setupOverlayColor();
//
//							Camera camera = client.gameRenderer.getCamera();
//							MatrixStack matrix = new MatrixStack();
//							if (!inGui) {
//								matrix.peek().getPosition().multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
//								matrix.peek().getPosition().multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(camera.getYaw()));
//								matrix.peek().getPosition().multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(camera.getPitch()));
//							}
//							matrix.peek().getPosition().multiply(matrixPositionClone);
//							matrix.peek().getNormal().multiply(matrixNormalClone);
//
//							for (BakedQuad quad : this.limlib$getQuads(renderPair.getSecond())) {
//								BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();
//								bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
//
//								renderPair.getFirst().setup(matrix, shader);
//
//								if (shader.getUniform("renderAsEntity") != null) {
//									shader.getUniform("renderAsEntity").setFloat(1.0F);
//								}
//
//								if (shader.chunkOffset != null) {
//									shader.chunkOffset.setVec3(0.0F, 0.0F, 0.0F);
//								}
//
//								int i = -1;
//								if (!copiedStack.isEmpty() && quad.hasColor()) {
//									i = this.colors.getColor(copiedStack, quad.getColorIndex());
//								}
//
//								float f = (float) (i >> 16 & 0xFF) / 255.0F;
//								float g = (float) (i >> 8 & 0xFF) / 255.0F;
//								float h = (float) (i & 0xFF) / 255.0F;
//
//								bufferBuilder.bakedQuad(matrix.peek(), quad, f, g, h, light, overlay);
//
//								BufferRenderer.drawWithShader(bufferBuilder.end());
//							}
//
//							client.gameRenderer.getOverlayTexture().teardownOverlayColor();
//							client.gameRenderer.getLightmapTextureManager().disable();
//							RenderSystem.polygonOffset(0.0F, 0.0F);
//							RenderSystem.disablePolygonOffset();
//							RenderSystem.disableBlend();
//
//						}
//					});
//				}
//			});
//
//			immediateRenderer.forEach(Runnable::run);
//			immediateRenderer.clear();
//		}
//	}

	@Inject(method = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemModel(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"))
	private void limlib$renderGuiItemModel$head(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
		inGui = true;
	}

	@Inject(method = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemModel(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("TAIL"))
	private void limlib$renderGuiItemModel$tail(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
		inGui = false;
	}

	@Override
	public boolean isInGui() {
		return inGui;
	}

	@Override
	public void setInGui(boolean in) {
		inGui = in;
	}

	@Unique
	private Set<BakedQuad> limlib$getQuads(BakedModel model) {
		Set<BakedQuad> bakedQuads = Sets.newHashSet();

		RandomGenerator randomGenerator = RandomGenerator.createLegacy();

		for (Direction dir : Direction.values()) {
			randomGenerator.setSeed(42L);
			bakedQuads.addAll(model.getQuads(null, dir, randomGenerator));
		}

		randomGenerator.setSeed(42L);
		bakedQuads.addAll(model.getQuads(null, null, randomGenerator));

		return bakedQuads;
	}

}
