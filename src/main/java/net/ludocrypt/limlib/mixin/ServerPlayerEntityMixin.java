package net.ludocrypt.limlib.mixin;

import java.util.Optional;
import java.util.regex.Pattern;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.api.sound.LiminalTravelSound;
import net.ludocrypt.limlib.api.sound.LiminalTravelSound.TravelRule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

	@Shadow
	public ServerPlayNetworkHandler networkHandler;

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Inject(method = "moveToWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 5, shift = Shift.AFTER))
	public void limlib$moveToWorld(ServerWorld to, CallbackInfoReturnable<Entity> ci) {
		ServerWorld from = ((ServerPlayerEntity) (Object) this).getWorld();
		SoundEvent sound = null;

		Optional<LiminalTravelSound> toTravelSound = ((DimensionTypeAccess) to.getDimension()).getLiminalEffects().getTravel();
		Optional<LiminalTravelSound> fromTravelSound = ((DimensionTypeAccess) from.getDimension()).getLiminalEffects().getTravel();

		if (toTravelSound.isPresent()) {
			LiminalTravelSound travel = toTravelSound.get();
			for (TravelRule rule : travel.getToTravelRules()) {
				if (Pattern.matches(rule.getRegex(), to.getRegistryKey().getValue().toString())) {
					sound = rule.getResult();
				}
			}
		}

		if (fromTravelSound.isPresent()) {
			LiminalTravelSound travel = fromTravelSound.get();
			for (TravelRule rule : travel.getFromTravelRules()) {
				if (Pattern.matches(rule.getRegex(), from.getRegistryKey().getValue().toString())) {
					sound = rule.getResult();
				}
			}
		}

		if (sound != null) {
			this.networkHandler.sendPacket(new PlaySoundS2CPacket(sound, SoundCategory.AMBIENT, this.getX(), this.getY(), this.getZ(), 0.25F, world.getRandom().nextFloat() * 0.4F + 0.8F));
		}
	}

	@ModifyArg(method = "moveToWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/WorldEventS2CPacket;<init>(ILnet/minecraft/util/math/BlockPos;IZ)V", ordinal = 0), index = 0)
	private int limlib$moveToWorld(int in) {
		return 29848748;
	}

}
