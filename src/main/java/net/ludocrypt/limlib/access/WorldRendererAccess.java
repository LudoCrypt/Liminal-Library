package net.ludocrypt.limlib.access;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

public interface WorldRendererAccess {

	public void renderSky(MatrixStack matrices, Matrix4f positionMatrix, float tickDelta);

	public void renderQuads(float tickDelta, MatrixStack matrices, Camera camera);

	public void renderHands(Framebuffer framebuffer, float tickDelta, MatrixStack matrices, Camera camera);

	public void renderItems(Framebuffer framebuffer, float tickDelta, MatrixStack matrices, Camera camera);

	public void renderBlocks(Framebuffer framebuffer, float tickDelta, MatrixStack matrices, Camera camera);

	public List<Pair<BlockPos, BlockState>> getQuadRenderData();

	public boolean isRenderingHands();

	public boolean isRenderingItems();

}
