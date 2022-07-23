package net.ludocrypt.limlib.mixin.client.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(GameRenderer.class)
public interface GameRendererAccessorTwo {

	@Invoker("renderHand")
	void callRenderHand(MatrixStack matrices, Camera camera, float tickDelta);

}
