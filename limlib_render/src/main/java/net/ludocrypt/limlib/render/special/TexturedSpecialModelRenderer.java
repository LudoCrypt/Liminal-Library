package net.ludocrypt.limlib.render.special;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;

public class TexturedSpecialModelRenderer extends SpecialModelRenderer {

	public static final SpecialModelRenderer TEXTURED = new TexturedSpecialModelRenderer();

	@Override
	public void setup(MatrixStack matrices, ShaderProgram shader) {
		RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
	}

}
