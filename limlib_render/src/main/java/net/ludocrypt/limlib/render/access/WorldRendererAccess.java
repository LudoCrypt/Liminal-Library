package net.ludocrypt.limlib.render.access;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.vertex.VertexBuffer;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

public interface WorldRendererAccess {

	public void render(MatrixStack matrices, Matrix4f positionMatrix, float tickDelta, Camera camera);

	public void renderHands(Framebuffer framebuffer, float tickDelta, MatrixStack matrices, Camera camera);

	public void renderItems(Framebuffer framebuffer, float tickDelta, MatrixStack matrices, Camera camera);

	public void renderSkyboxes(MatrixStack matrices, Matrix4f positionMatrix, float tickDelta);

	public void renderBlocks(MatrixStack matrices, Matrix4f positionMatrix);

	public void renderBuffer(MatrixStack matrices, Matrix4f positionMatrix, Identifier shaderId, VertexBuffer buffer, BakedQuad quad, BlockPos origin);

	public boolean isRenderingHands();

	public boolean isRenderingItems();

}
