package net.ludocrypt.limlib.mixin.client.render.chunk.sodium;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import net.ludocrypt.limlib.access.RenderDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@Pseudo
@Mixin(RenderSectionManager.class)
public class RenderSectionManagerMixin implements RenderDataAccess {

	@Unique
	private HashMap<BlockPos, BlockState> quadData = Maps.newHashMap();

	@Group(name = "addVisible", min = 1)
	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSectionManager;addVisible(Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;Lnet/minecraft/util/math/Direction;)V", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSectionManager;addEntitiesToRenderLists(Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;)V", shift = Shift.AFTER), remap = false, require = 0)
	private void limlib$addVisible$mapped(RenderSection render, Direction flow, CallbackInfo ci) {
		quadData.putAll(((RenderDataAccess) render.getData()).getCustomQuadData());
	}

	@Group(name = "addVisible", min = 1)
	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSectionManager;addVisible(Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;Lnet/minecraft/class_2350;)V", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSectionManager;addEntitiesToRenderLists(Lme/jellysquid/mods/sodium/client/render/chunk/RenderSection;)V", shift = Shift.AFTER), remap = false, require = 0)
	private void limlib$addVisible$unmapped(RenderSection render, Direction flow, CallbackInfo ci) {
		quadData.putAll(((RenderDataAccess) render.getData()).getCustomQuadData());
	}

	@Inject(method = "Lme/jellysquid/mods/sodium/client/render/chunk/RenderSectionManager;resetLists()V", at = @At("TAIL"), remap = false)
	private void limlib$resetLists(CallbackInfo ci) {
		quadData.clear();
	}

	@Override
	public HashMap<BlockPos, BlockState> getCustomQuadData() {
		return quadData;
	}

}
