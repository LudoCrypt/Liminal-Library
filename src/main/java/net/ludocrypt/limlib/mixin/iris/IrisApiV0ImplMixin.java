package net.ludocrypt.limlib.mixin.iris;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.coderbot.iris.IrisApiV0Impl;
import net.ludocrypt.limlib.access.IrisForceDisableShadersAccess;
import net.minecraft.client.MinecraftClient;

@Mixin(IrisApiV0Impl.class)
public class IrisApiV0ImplMixin {

	@Inject(method = "Lnet/coderbot/iris/IrisApiV0Impl;isShaderPackInUse()Z", at = @At("HEAD"), cancellable = true)
	public void limlib$isShaderPackInUse(CallbackInfoReturnable<Boolean> ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (((IrisForceDisableShadersAccess) client.gameRenderer).shouldForceDisableShaders()) {
			ci.setReturnValue(false);
		}
	}

}
