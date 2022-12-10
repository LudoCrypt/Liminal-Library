package net.ludocrypt.limlib.render.mixin.sodium;

//import java.util.Map;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Pseudo;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import com.google.common.collect.Maps;
//import com.mojang.blaze3d.vertex.VertexBuffer;
//import com.mojang.datafixers.util.Pair;
//
//import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
//import net.ludocrypt.limlib.render.access.sodium.RenderSectionAccess;
//import net.minecraft.client.render.model.BakedQuad;
//import net.minecraft.util.Identifier;
//
//@Pseudo
//@Mixin(RenderSection.class)
//public class RenderSectionMixin implements RenderSectionAccess {
//
//	@Unique
//	private Map<BakedQuad, Pair<VertexBuffer, Identifier>> vertexBufferMap;
//
//	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;deleteGraphicsState()V", at = @At("HEAD"), remap = false)
//	private void limlib$deleteGraphicsState(CallbackInfo ci) {
//		this.getVertexBufferMap().forEach((quad, renderPair) -> renderPair.getFirst().close());
//		this.getVertexBufferMap().clear();
//	}
//
//	@Override
//	public Map<BakedQuad, Pair<VertexBuffer, Identifier>> getVertexBufferMap() {
//		if (vertexBufferMap == null) {
//			vertexBufferMap = Maps.newHashMap();
//		}
//		return vertexBufferMap;
//	}
//
//}
