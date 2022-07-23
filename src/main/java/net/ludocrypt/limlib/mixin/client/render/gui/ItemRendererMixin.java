package net.ludocrypt.limlib.mixin.client.render.gui;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.limlib.access.IrisClientAccess;
import net.ludocrypt.limlib.access.ItemRendererAccess;
import net.ludocrypt.limlib.access.ModelAccess;
import net.ludocrypt.limlib.access.WorldRendererAccess;
import net.ludocrypt.limlib.impl.LimlibRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin implements ItemRendererAccess {

	@Unique
	private boolean inGui = false;

	@Inject(method = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V", at = @At("HEAD"))
	private void limlib$renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();

		boolean isHandRendering = ((WorldRendererAccess) client.worldRenderer).isRenderingHands() || (FabricLoader.getInstance().isModLoaded("iris") && ((IrisClientAccess) client).isHandRenderingActive());
		boolean isItemRendering = ((WorldRendererAccess) client.worldRenderer).isRenderingItems();

		if (isHandRendering || isItemRendering || inGui) {
			MatrixStack matrixStack = new MatrixStack();
			matrixStack.multiplyPositionMatrix(matrices.peek().getPositionMatrix().copy());
			List<Runnable> immediateRenderer = Lists.newArrayList();
			LimlibRendering.LIMINAL_QUAD_RENDERER.get(((ModelAccess) model).getLiminalQuadRenderer().get()).renderItemModel(model, client.world, stack.copy(), matrixStack, client.gameRenderer.getCamera(), inGui);
			immediateRenderer.forEach(Runnable::run);
			immediateRenderer.clear();
		}
	}

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
}
