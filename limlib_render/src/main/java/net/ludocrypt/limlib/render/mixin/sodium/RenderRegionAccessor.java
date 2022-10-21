package net.ludocrypt.limlib.render.mixin.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(RenderRegion.class)
public interface RenderRegionAccessor {

	@Accessor
	Set<RenderSection> getChunks();

}
