package net.ludocrypt.limlib.registry.util;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.worldgen.dimension.api.QuiltDimensions;

import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.gen.DensityFunctions;

public class LimlibUtil implements ModInitializer {

	public static SoundEvent travelingSound = null;
	public static float travelingVolume = 0.25F;
	public static float travelingPitch = 1.0F;

	@Override
	public void onInitialize(ModContainer mod) {
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> {
			dispatcher.register(CommandManager.literal("limlib").requires(source -> source.hasPermissionLevel(2)).then(CommandManager.literal("tp").then(CommandManager.argument("entity", EntityArgumentType.entities()).then(CommandManager.argument("destination", DimensionArgumentType.dimension()).executes((source) -> {
				Collection<? extends Entity> entities = EntityArgumentType.getEntities(source, "entity");
				for (Entity entity : entities) {
					ServerWorld world = DimensionArgumentType.getDimensionArgument(source, "destination");
					Vec3d pos = entity.getPos();
					Vec2f vec = new Vec2f(entity.getYaw(), entity.getPitch());
					SoundEvent sound = SoundEvents.BLOCK_PORTAL_TRAVEL;
					float volume = 0.25F;
					float pitch = 1.0F;
					travelTo(entity, world, new TeleportTarget(pos, entity.getVelocity(), vec.y, vec.x), sound, volume, pitch);
				}
				return 1;
			}).then(CommandManager.argument("location", Vec3ArgumentType.vec3()).executes((source) -> {
				Collection<? extends Entity> entities = EntityArgumentType.getEntities(source, "entity");
				for (Entity entity : entities) {
					ServerWorld world = DimensionArgumentType.getDimensionArgument(source, "destination");
					Vec3d pos = Vec3ArgumentType.getVec3(source, "location");
					Vec2f vec = new Vec2f(entity.getYaw(), entity.getPitch());
					SoundEvent sound = SoundEvents.BLOCK_PORTAL_TRAVEL;
					float volume = 0.25F;
					float pitch = 1.0F;
					travelTo(entity, world, new TeleportTarget(pos, entity.getVelocity(), vec.y, vec.x), sound, volume, pitch);
				}
				return 1;
			}).then(CommandManager.argument("angle", RotationArgumentType.rotation()).executes((source) -> {
				Collection<? extends Entity> entities = EntityArgumentType.getEntities(source, "entity");
				for (Entity entity : entities) {
					ServerWorld world = DimensionArgumentType.getDimensionArgument(source, "destination");
					Vec3d pos = Vec3ArgumentType.getVec3(source, "location");
					Vec2f vec = RotationArgumentType.getRotation(source, "angle").toAbsoluteRotation(source.getSource());
					SoundEvent sound = SoundEvents.BLOCK_PORTAL_TRAVEL;
					float volume = 0.25F;
					float pitch = 1.0F;
					travelTo(entity, world, new TeleportTarget(pos, entity.getVelocity(), vec.y, vec.x), sound, volume, pitch);
				}
				return 1;
			}).then(CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS).executes((source) -> {
				Collection<? extends Entity> entities = EntityArgumentType.getEntities(source, "entity");
				for (Entity entity : entities) {
					ServerWorld world = DimensionArgumentType.getDimensionArgument(source, "destination");
					Vec3d pos = Vec3ArgumentType.getVec3(source, "location");
					Vec2f vec = RotationArgumentType.getRotation(source, "angle").toAbsoluteRotation(source.getSource());
					SoundEvent sound = SoundEvent.createVariableRangeEvent(IdentifierArgumentType.getIdentifier(source, "sound"));
					float volume = 0.25F;
					float pitch = 1.0F;
					travelTo(entity, world, new TeleportTarget(pos, entity.getVelocity(), vec.y, vec.x), sound, volume, pitch);
				}
				return 1;
			}).then(CommandManager.argument("volume", FloatArgumentType.floatArg(0.0F)).executes((source) -> {
				Collection<? extends Entity> entities = EntityArgumentType.getEntities(source, "entity");
				for (Entity entity : entities) {
					ServerWorld world = DimensionArgumentType.getDimensionArgument(source, "destination");
					Vec3d pos = Vec3ArgumentType.getVec3(source, "location");
					Vec2f vec = RotationArgumentType.getRotation(source, "angle").toAbsoluteRotation(source.getSource());
					SoundEvent sound = SoundEvent.createVariableRangeEvent(IdentifierArgumentType.getIdentifier(source, "sound"));
					float volume = FloatArgumentType.getFloat(source, "volume");
					float pitch = 1.0F;
					travelTo(entity, world, new TeleportTarget(pos, entity.getVelocity(), vec.y, vec.x), sound, volume, pitch);
				}
				return 1;
			}).then(CommandManager.argument("pitch", FloatArgumentType.floatArg(0.0F, 2.0F)).executes((source) -> {
				Collection<? extends Entity> entities = EntityArgumentType.getEntities(source, "entity");
				for (Entity entity : entities) {
					ServerWorld world = DimensionArgumentType.getDimensionArgument(source, "destination");
					Vec3d pos = Vec3ArgumentType.getVec3(source, "location");
					Vec2f vec = RotationArgumentType.getRotation(source, "angle").toAbsoluteRotation(source.getSource());
					SoundEvent sound = SoundEvent.createVariableRangeEvent(IdentifierArgumentType.getIdentifier(source, "sound"));
					float volume = FloatArgumentType.getFloat(source, "volume");
					float pitch = FloatArgumentType.getFloat(source, "pitch");
					travelTo(entity, world, new TeleportTarget(pos, entity.getVelocity(), vec.y, vec.x), sound, volume, pitch);
				}
				return 1;
			}))))))))));
		});
	}

	public static <E extends Entity> E travelTo(E teleported, ServerWorld destination, TeleportTarget target, SoundEvent sound, float volume, float pitch) {
		if (destination.equals(teleported.getWorld())) {

			BlockPos blockPos = new BlockPos(target.position);
			if (!World.isValid(blockPos)) {
				throw new UnsupportedOperationException("Position " + blockPos.toString() + " is out of this world!");
			}

			float f = MathHelper.wrapDegrees(target.yaw);
			float g = MathHelper.wrapDegrees(target.pitch);

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
				return QuiltDimensions.teleport(teleported, destination, target);
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
		return new MultiNoiseSampler(DensityFunctions.constant(d1), DensityFunctions.constant(d2), DensityFunctions.constant(d3), DensityFunctions.constant(d4), DensityFunctions.constant(d5), DensityFunctions.constant(d6), List.of());
	}

	public static MultiNoiseSampler createMultiNoiseSampler(double d) {
		return createMultiNoiseSampler(d, d, d, d, d, d);
	}

	public static MultiNoiseSampler createMultiNoiseSampler() {
		return createMultiNoiseSampler(0.0D);
	}

}
