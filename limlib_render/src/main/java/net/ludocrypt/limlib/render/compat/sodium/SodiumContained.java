package net.ludocrypt.limlib.render.compat.sodium;

//import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
//import net.ludocrypt.limlib.render.access.WorldRendererAccess;
//import net.ludocrypt.limlib.render.access.sodium.RenderSectionAccess;
//import net.ludocrypt.limlib.render.mixin.sodium.RenderSectionManagerAccessor;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Matrix4f;
//
//public class SodiumContained {
//
//	public static void renderSodiumBlocks(MatrixStack matrices, Matrix4f positionMatrix, WorldRendererAccess world) {
//		((RenderSectionManagerAccessor) SodiumWorldRenderer.instance().getRenderSectionManager()).getSections().values().forEach((renderSection) -> {
//			((RenderSectionAccess) renderSection).getVertexBufferMap().forEach((quad, renderPair) -> {
//				world.renderBuffer(matrices, positionMatrix, renderPair.getSecond(), renderPair.getFirst(), quad, new BlockPos(renderSection.getOriginX(), renderSection.getOriginY(), renderSection.getOriginZ()));
//			});
//		});
//	}
//
//}
