package net.ludocrypt.limlib.render.util.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

/**
 * 
 * A skybox thats textured with 6 textures for each cardinal direction.
 *
 */
public class TexturedSkybox extends Skybox {

	public static final Codec<TexturedSkybox> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Identifier.CODEC.fieldOf("skybox").stable().forGetter((sky) -> {
			return sky.identifier;
		})).apply(instance, instance.stable(TexturedSkybox::new));
	});

	public final Identifier identifier;

	public TexturedSkybox(Identifier identifier) {
		this.identifier = identifier;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderSky(WorldRenderer worldRenderer, MinecraftClient client, MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());

		Vec3d color = client.world.getSkyColor(client.gameRenderer.getCamera().getPos(), tickDelta).multiply(255);
		int r = (int) Math.floor(color.x);
		int g = (int) Math.floor(color.y);
		int b = (int) Math.floor(color.z);
		int a = 255;
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();

		for (int i = 0; i < 6; ++i) {
			matrices.push();
			if (i == 0) {
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
			}

			if (i == 1) {
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
			}

			if (i == 2) {
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
			}

			if (i == 3) {
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
			}

			if (i == 4) {
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
			}

			Matrix4f matrix4f = matrices.peek().getPosition();

			RenderSystem.setShaderTexture(0, new Identifier(identifier.toString() + "_" + i + ".png"));
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.0F, 0.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.0F, 1.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(1.0F, 1.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(1.0F, 0.0F).color(r, g, b, a).next();
			BufferRenderer.drawWithShader(bufferBuilder.end());
			matrices.pop();
		}

		RenderSystem.depthMask(true);
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}

	@Override
	public Codec<? extends Skybox> getCodec() {
		return CODEC;
	}

}
