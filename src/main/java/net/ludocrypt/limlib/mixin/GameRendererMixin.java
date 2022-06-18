package net.ludocrypt.limlib.mixin;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.limlib.access.BakedModelAccess;
import net.ludocrypt.limlib.access.HeldItemRendererAccess;
import net.ludocrypt.limlib.access.IrisForceDisableShadersAccess;
import net.ludocrypt.limlib.access.QuadRenderingAccess;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Program;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements IrisForceDisableShadersAccess, QuadRenderingAccess {

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	@Final
	private BufferBuilderStorage buffers;

	@Unique
	private boolean shouldForceDisableShaders;

	@Unique
	private boolean isQuadRendering;

	@Inject(method = "loadShaders", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 53, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$loadShaders(ResourceManager manager, CallbackInfo ci, List<Program> list, List<Pair<Shader, Consumer<Shader>>> list2) {
		LimlibRegistries.LIMINAL_CORE_SHADER.forEach((core) -> {
			try {
				Identifier id = LimlibRegistries.LIMINAL_CORE_SHADER.getId(core);
				list2.add(Pair.of(new Shader(manager, "rendertype_" + id.getNamespace() + "_" + id.getPath(), core.getVertexFormat()), core::setShader));
			} catch (IOException e) {
				list2.forEach((pair) -> {
					pair.getFirst().close();
				});
				throw new RuntimeException("could not reload shaders", e);
			}
		});
	}

	@Inject(method = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Camera;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", shift = Shift.AFTER))
	private void limlib$renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
		isQuadRendering = true;
		shouldForceDisableShaders = true;

		HeldItemRenderer heldItemRenderer = client.getEntityRenderDispatcher().getHeldItemRenderer();

		heldItemRenderer.renderItem(tickDelta, matrices, this.buffers.getEntityVertexConsumers(), client.player, 0);

		MatrixStack leftStack = ((HeldItemRendererAccess) heldItemRenderer).getLeftHandStack();
		MatrixStack rightStack = ((HeldItemRendererAccess) heldItemRenderer).getRightHandStack();

		boolean leftHanded = client.player.getMainArm() == Arm.LEFT;

		ItemStack mainHand = client.player.getStackInHand(Hand.MAIN_HAND);
		BakedModel mainModel = client.getItemRenderer().getModel(mainHand, client.world, client.player, 0);

		mainModel.getTransformation().getTransformation(ModelTransformation.Mode.FIXED).apply(leftHanded, leftHanded ? leftStack : rightStack);
		(leftHanded ? leftStack : rightStack).translate(-0.5, -0.5, -0.5);

		((BakedModelAccess) mainModel).getSubQuads().forEach((id, quads) -> LimlibRegistries.LIMINAL_QUAD_RENDERER.get(id).renderQuads(quads, client.world, Optional.empty(), Either.right(mainHand), leftHanded ? leftStack : rightStack, camera, true));

		ItemStack offHand = client.player.getStackInHand(Hand.OFF_HAND);
		BakedModel offModel = client.getItemRenderer().getModel(offHand, client.world, client.player, 0);

		offModel.getTransformation().getTransformation(ModelTransformation.Mode.FIXED).apply(!leftHanded, !leftHanded ? leftStack : rightStack);
		(!leftHanded ? leftStack : rightStack).translate(-0.5, -0.5, -0.5);

		((BakedModelAccess) offModel).getSubQuads().forEach((id, quads) -> LimlibRegistries.LIMINAL_QUAD_RENDERER.get(id).renderQuads(quads, client.world, Optional.empty(), Either.right(offHand), !leftHanded ? leftStack : rightStack, camera, true));

		((HeldItemRendererAccess) heldItemRenderer).setLeftHandStack(new MatrixStack());
		((HeldItemRendererAccess) heldItemRenderer).setRightHandStack(new MatrixStack());

		shouldForceDisableShaders = false;
		isQuadRendering = false;
	}

	@Override
	public boolean shouldForceDisableShaders() {
		return shouldForceDisableShaders;
	}

	@Override
	public void setShouldForceDisableShaders(boolean disable) {
		shouldForceDisableShaders = disable;
	}

	@Override
	public boolean isQuadRendering() {
		return isQuadRendering;
	}

}
