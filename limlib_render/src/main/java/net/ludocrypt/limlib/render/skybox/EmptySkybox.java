package net.ludocrypt.limlib.render.skybox;

import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * 
 * A skybox that does nothing.
 * 
 **/
public class EmptySkybox extends Skybox {

	public static final Codec<EmptySkybox> CODEC = RecordCodecBuilder.create((instance) -> instance.stable(new EmptySkybox()));

	@Override
	@ClientOnly
	public void renderSky(WorldRenderer worldRenderer, MinecraftClient client, MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta) {
	}

	@Override
	public Codec<? extends Skybox> getCodec() {
		return CODEC;
	}

}
