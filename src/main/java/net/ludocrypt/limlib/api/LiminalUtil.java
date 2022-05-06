package net.ludocrypt.limlib.api;

import java.util.List;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public class LiminalUtil {

	public static SoundEvent travelingSound = null;
	public static float travelingVolume = 1.0F;
	public static float travelingPitch = 1.0F;

	public static <E extends Entity> E travelTo(E teleported, ServerWorld destination, TeleportTarget target, SoundEvent sound, float volume, float pitch) {
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
