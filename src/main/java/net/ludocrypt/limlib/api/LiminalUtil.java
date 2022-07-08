package net.ludocrypt.limlib.api;

import java.util.EnumSet;
import java.util.List;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public class LiminalUtil {

	public static SoundEvent travelingSound = null;
	public static float travelingVolume = 1.0F;
	public static float travelingPitch = 1.0F;

	public static <E extends Entity> E travelTo(E teleported, ServerWorld destination, TeleportTarget target, SoundEvent sound, float volume, float pitch) {
		if (destination.equals(teleported.getWorld())) {

			BlockPos blockPos = new BlockPos(target.position);
			if (!World.isValid(blockPos)) {
				throw new UnsupportedOperationException("Position " + blockPos.toString() + " is out of this world!");
			}

			float f = MathHelper.wrapDegrees(target.pitch);
			float g = MathHelper.wrapDegrees(target.yaw);

			if (teleported instanceof ServerPlayerEntity) {
				ChunkPos chunkPos = new ChunkPos(blockPos);
				destination.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, teleported.getId());
				teleported.stopRiding();
				if (((ServerPlayerEntity) teleported).isSleeping()) {
					((ServerPlayerEntity) teleported).wakeUp(true, true);
				}
				((ServerPlayerEntity) teleported).networkHandler.requestTeleport(target.position.x, target.position.y, target.position.z, f, g, EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class));

				teleported.setHeadYaw(f);
			} else {
				float h = MathHelper.clamp(g, -90.0f, 90.0f);
				teleported.refreshPositionAndAngles(target.position.x, target.position.y, target.position.z, f, h);
				teleported.setHeadYaw(f);
			}

			teleported.setVelocity(target.velocity);
			teleported.world.playSound(null, teleported.getX(), teleported.getY(), teleported.getZ(), sound, SoundCategory.AMBIENT, volume, pitch);

			return teleported;
		} else {
			try {
				travelingSound = sound;
				travelingVolume = volume;
				travelingPitch = pitch;
				return FabricDimensions.teleport(teleported, destination, target);
			} finally {
				travelingSound = null;
				travelingVolume = 0.0F;
				travelingPitch = 0.0F;
			}
		}
	}

	public static void grantAdvancement(PlayerEntity player, Identifier id) {
		if (player instanceof ServerPlayerEntity serverPlayerEntity) {
			Advancement advancement = serverPlayerEntity.server.getAdvancementLoader().get(id);
			AdvancementProgress progress = serverPlayerEntity.getAdvancementTracker().getProgress(advancement);
			if (!progress.isDone()) {
				progress.getUnobtainedCriteria().forEach((criteria) -> serverPlayerEntity.getAdvancementTracker().grantCriterion(advancement, criteria));
			}
		}
	}

	public static MultiNoiseSampler createMultiNoiseSampler(double d1, double d2, double d3, double d4, double d5, double d6) {
		return new MultiNoiseSampler(DensityFunctionTypes.constant(d1), DensityFunctionTypes.constant(d2), DensityFunctionTypes.constant(d3), DensityFunctionTypes.constant(d4), DensityFunctionTypes.constant(d5), DensityFunctionTypes.constant(d6), List.of());
	}

	public static MultiNoiseSampler createMultiNoiseSampler(double d) {
		return createMultiNoiseSampler(d, d, d, d, d, d);
	}

	public static MultiNoiseSampler createMultiNoiseSampler() {
		return createMultiNoiseSampler(0.0D);
	}

}
