package net.ludocrypt.limlib.mixin.iris;

import org.spongepowered.asm.mixin.Mixin;

import net.irisshaders.iris.api.v0.IrisApi;
import net.ludocrypt.limlib.access.IrisClientAccess;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IrisClientAccess {

	@Override
	public boolean areShadersInUse() {
		return IrisApi.getInstance().isShaderPackInUse();
	}

}
