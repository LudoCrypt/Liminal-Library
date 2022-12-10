package net.ludocrypt.limlib.render.compat.sodium;

import org.quiltmc.loader.api.QuiltLoader;

import net.ludocrypt.limlib.render.access.WorldRendererAccess;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class SodiumBridge {

	public static final boolean SODIUM_LOADED = QuiltLoader.isModLoaded("sodium");

	public static void renderSodiumBlocks(MatrixStack matrices, Matrix4f positionMatrix, WorldRendererAccess world) {
//		if (SODIUM_LOADED) {
//			SodiumContained.renderSodiumBlocks(matrices, positionMatrix, world);
//		}
	}

}
