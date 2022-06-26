package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.IrisClientAccess;
import net.ludocrypt.limlib.access.WorldRendererAccess;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

	@Inject(method = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V", at = @At("HEAD"))
	private void limlib$renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();

		boolean isHandRendering = ((WorldRendererAccess) client.worldRenderer).isRenderingHands() || (FabricLoader.getInstance().isModLoaded("iris") && ((IrisClientAccess) client).isHandRenderingActive());
		boolean isItemRendering = ((WorldRendererAccess) client.worldRenderer).isRenderingItems();

		if (isHandRendering || isItemRendering) {
			MatrixStack matrixStack = new MatrixStack();
			matrixStack.multiplyPositionMatrix(matrices.peek().getPositionMatrix().copy());

			((BakedModelAccess) model).getSubQuads().forEach((id, quads) -> (isHandRendering ? LimlibRegistries.LIMINAL_QUAD_RENDERER.get(id).heldItemRenderQueue : LimlibRegistries.LIMINAL_QUAD_RENDERER.get(id).itemRenderQueue).add(() -> LimlibRegistries.LIMINAL_QUAD_RENDERER.get(id).renderItemQuads(quads, client.world, stack.copy(), matrixStack, client.gameRenderer.getCamera())));
		}
	}
}
