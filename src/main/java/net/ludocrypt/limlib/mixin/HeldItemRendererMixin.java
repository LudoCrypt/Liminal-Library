package net.ludocrypt.limlib.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.ludocrypt.limlib.access.HeldItemRendererAccess;
import net.ludocrypt.limlib.access.QuadRenderingAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin implements HeldItemRendererAccess {

	@Shadow
	@Final
	private MinecraftClient client;

	@Unique
	MatrixStack leftHandStack = new MatrixStack(), rightHandStack = new MatrixStack();

	@Redirect(method = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
	private void limlib$renderItem(HeldItemRenderer heldItemRenderer, LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		if (!((QuadRenderingAccess) client.gameRenderer).isQuadRendering()) {
			heldItemRenderer.renderItem(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, light);
		} else {
			(leftHanded ? leftHandStack : rightHandStack).push();
			(leftHanded ? leftHandStack : rightHandStack).multiplyPositionMatrix(matrices.peek().getPositionMatrix());
		}
	}

	@Redirect(method = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw()V"))
	private void limlib$draw(VertexConsumerProvider.Immediate vertexConsumers) {
		if (!((QuadRenderingAccess) client.gameRenderer).isQuadRendering()) {
			vertexConsumers.draw();
		}
	}

	@Override
	public MatrixStack getLeftHandStack() {
		return leftHandStack;
	}

	@Override
	public MatrixStack getRightHandStack() {
		return rightHandStack;
	}

	@Override
	public void setLeftHandStack(MatrixStack stack) {
		leftHandStack = stack;
	}

	@Override
	public void setRightHandStack(MatrixStack stack) {
		rightHandStack = stack;
	}

}
