package net.ludocrypt.limlib.render.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.ludocrypt.limlib.render.access.WorldRendererAccess;
import net.ludocrypt.limlib.render.access.sodium.RenderSectionAccess;
import net.ludocrypt.limlib.render.access.sodium.SodiumWorldRendererAccess;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements SodiumWorldRendererAccess, WorldRendererAccess {

	@Override
	public void renderSodiumBlocks(MatrixStack matrices, Matrix4f positionMatrix) {
		((RenderSectionManagerAccessor) SodiumWorldRenderer.instance().getRenderSectionManager()).getSections().values().forEach((renderSection) -> {
			((RenderSectionAccess) renderSection).getVertexBufferMap().forEach((quad, renderPair) -> {
				renderBuffer(matrices, positionMatrix, renderPair.getSecond(), renderPair.getFirst(), quad, new BlockPos(renderSection.getOriginX(), renderSection.getOriginY(), renderSection.getOriginZ()));
			});
		});
	}

}
