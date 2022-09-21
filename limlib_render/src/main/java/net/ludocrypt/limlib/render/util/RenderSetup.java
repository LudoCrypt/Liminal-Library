package net.ludocrypt.limlib.render.util;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@FunctionalInterface
public interface RenderSetup {

	public void setup(MatrixStack matrices, Identifier shaderId, BakedQuad quad);

}
