package net.ludocrypt.limlib.mixin.client.render.chunk.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;

@Mixin(SodiumWorldRenderer.class)
public interface SodiumWorldRendererAccessor {

	@Accessor
	RenderSectionManager getRenderSectionManager();

}
