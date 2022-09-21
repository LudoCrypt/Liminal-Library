package net.ludocrypt.limlib.render.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;

import net.ludocrypt.limlib.render.access.SodiumWorldRendererAccess;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

@Mixin(value = WorldRenderer.class, priority = 900)
public abstract class WorldRendererMixin implements SodiumWorldRendererAccess {

	@Override
	public void renderSodiumBlocks(MatrixStack matrices, Matrix4f positionMatrix) {
	}

}
