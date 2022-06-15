package net.ludocrypt.limlib;

import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.ludocrypt.limlib.access.DimensionEffectsAccess;
import net.ludocrypt.limlib.api.render.LiminalCoreShader;
import net.ludocrypt.limlib.api.render.LiminalQuadRenderer;
import net.ludocrypt.limlib.api.render.LiminalShaderApplier;
import net.ludocrypt.limlib.api.sound.SoundEmitter;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

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

		LiminalCoreShader core = Registry.register(LimlibRegistries.LIMINAL_CORE_SHADER, new Identifier("limlib", "skybox_clone"), new LiminalCoreShader(VertexFormats.POSITION));

		Registry.register(LimlibRegistries.LIMINAL_QUAD_RENDERER, new Identifier("limlib", "black"), new LiminalQuadRenderer() {

			@Override
			public void renderQuad(BakedQuad quad, BufferBuilder bufferBuilder, Matrix4f matrix, Camera camera, World world, Optional<BlockPos> pos, Either<BlockState, ItemStack> item, MatrixStack matrices, List<Pair<BakedQuad, Optional<Direction>>> quads, boolean renderInGui) {
				RenderSystem.setShader(core::getShader);

				for (int i = 0; i < 6; i++) {
					RenderSystem.setShaderTexture(i, new Identifier(quad.getSprite().getId().getNamespace(), "textures/" + quad.getSprite().getId().getPath() + ".png"));
				}

				LiminalQuadRenderer.quad((vec3f) -> bufferBuilder.vertex(vec3f.getX(), vec3f.getY(), vec3f.getZ()).next(), matrix, quad);
			}

			@Override
			public boolean renderBehind() {
				return true;
			}

			@Override
			public VertexFormat vertexFormat() {
				return VertexFormats.POSITION;
			}

			@Override
			public DrawMode drawMode() {
				return DrawMode.QUADS;
			}
		});
	}

}
