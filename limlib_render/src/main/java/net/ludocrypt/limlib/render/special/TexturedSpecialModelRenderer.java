package net.ludocrypt.limlib.render.special;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TexturedSpecialModelRenderer extends SpecialModelRenderer {

	public static final Codec<TexturedSpecialModelRenderer> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Identifier.CODEC.fieldOf("texture").stable().forGetter((sky) -> {
			return sky.texture;
		})).apply(instance, instance.stable(TexturedSpecialModelRenderer::new));
	});

	public final Identifier texture;

	public TexturedSpecialModelRenderer(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public Codec<? extends SpecialModelRenderer> getCodec() {
		return CODEC;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TexturedSpecialModelRenderer smr) {
			return smr.texture == this.texture;
		}

		return super.equals(obj);
	}

	@Override
	public void setup(MatrixStack matrices, ShaderProgram shader) {
		RenderSystem.setShaderTexture(0, new Identifier(texture.getNamespace(), "textures/" + texture.getPath() + ".png"));
	}

}
