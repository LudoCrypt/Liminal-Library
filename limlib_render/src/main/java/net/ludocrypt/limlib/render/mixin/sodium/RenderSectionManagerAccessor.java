package net.ludocrypt.limlib.render.mixin.sodium;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;

@Mixin(RenderSectionManager.class)
public interface RenderSectionManagerAccessor {

	@Accessor(remap = false)
	Long2ReferenceMap<RenderSection> getSections();

}
