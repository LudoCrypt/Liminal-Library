package net.ludocrypt.limlib.impl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.api.LimlibTravelling.Travelling;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;

@Mixin(Entity.class)
public class EntityMixin implements Travelling {

	@Unique
	public TeleportTarget limlib$overriddenTeleportTarget = null;

	@Inject(method = "getTeleportTarget(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/world/TeleportTarget;", at = @At("HEAD"), cancellable = true)
	public void limlib$getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cir) {

		if (this.limlib$overriddenTeleportTarget != null) {
			cir.setReturnValue(this.limlib$overriddenTeleportTarget);
		}

	}

	@Override
	public TeleportTarget limlib$getTeleportTarget() {
		return limlib$overriddenTeleportTarget;
	}

	@Override
	public void limlib$setTeleportTarget(TeleportTarget teleportTarget) {
		this.limlib$overriddenTeleportTarget = teleportTarget;
	}

}
