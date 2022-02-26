package net.ludocrypt.limlib.api;

import java.util.Optional;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.ludocrypt.limlib.access.DimensionTypeAccess;
import net.ludocrypt.limlib.api.render.LiminalShader;
import net.ludocrypt.limlib.api.sound.RandomSoundEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LimLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
			MinecraftClient client = MinecraftClient.getInstance();
			Optional<LiminalShader> shader = ((DimensionTypeAccess) client.world.getDimension()).getLiminalEffects().getShader();
			if (shader.isPresent()) {
				if (shader.get().shouldRender(client, tickDelta)) {
					shader.get().getShader(client, tickDelta).render(tickDelta);
				}
			}
		});

		ClientTickEvents.START_WORLD_TICK.register((world) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			ClientPlayerEntity player = client.player;
			Iterable<BlockPos> iterable = BlockPos.iterateOutwards(player.getBlockPos(), 8, 8, 8);
			for (BlockPos pos : iterable) {
				if (pos.isWithinDistance(player.getBlockPos(), 16)) {
					BlockState state = world.getBlockState(pos);
					if (state != null) {
						if (state.getBlock()instanceof RandomSoundEmitter rse) {
							if (world.getRandom().nextDouble() < rse.getChance(world, state, pos)) {
								Vec3d relativePos = rse.getRelativePos(world, state, pos);
								world.playSound(null, pos.getX() + relativePos.getX(), pos.getY() + relativePos.getY(), pos.getZ() + relativePos.getZ(), rse.getSound(world, state, pos), rse.getSoundCategory(world, state, pos), rse.getVolume(world, state, pos), rse.getPitch(world, state, pos));
							}
						}
					}
				}
			}
		});
	}

}
