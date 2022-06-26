package net.ludocrypt.limlib;

import java.util.Optional;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.ludocrypt.limlib.api.render.LiminalShaderApplier;
import net.ludocrypt.limlib.api.sound.SoundEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class LimlibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
			MinecraftClient client = MinecraftClient.getInstance();
			Optional<LiminalShaderApplier> shader = ((DimensionEffectsAccess) (Object) client.world.getDimension()).getLiminalEffects().getShader();
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
					if (state.getBlock()instanceof SoundEmitter emitter) {
						emitter.playSound(client, world, pos, state);
					}
				}
			}
		});
	}

}
