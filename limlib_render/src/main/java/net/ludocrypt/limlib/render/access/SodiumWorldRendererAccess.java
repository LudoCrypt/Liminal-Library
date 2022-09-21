package net.ludocrypt.limlib.render.access;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public interface SodiumWorldRendererAccess {

	public void renderSodiumBlocks(MatrixStack matrices, Matrix4f positionMatrix);

}
