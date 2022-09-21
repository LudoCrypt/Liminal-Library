package net.ludocrypt.limlib.world.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.datafixers.util.Either;

import net.ludocrypt.limlib.world.chunk.AbstractNbtChunkGenerator;
import net.ludocrypt.limlib.world.chunk.LiminalChunkGenerator;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.BelowZeroRetrogen;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(ChunkStatus.class)
public class ChunkStatusMixin {

	@Inject(method = "m_meahrkeh(Lnet/minecraft/world/chunk/ChunkStatus;Ljava/util/concurrent/Executor;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/structure/StructureTemplateManager;Lnet/minecraft/server/world/ServerLightingProvider;Ljava/util/function/Function;Ljava/util/List;Lnet/minecraft/world/chunk/Chunk;Z)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
	private static void limlib$liminalChunkGenerator(ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk2, boolean regenerate, CallbackInfoReturnable<CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> ci) {
		if (generator instanceof LiminalChunkGenerator limChunkGen) {
			if (generator instanceof AbstractNbtChunkGenerator nbt) {
				if (nbt.loadedStructures.isEmpty()) {
					nbt.storeStructures(world);
				}
			}

			if (regenerate || !chunk2.getStatus().isAtLeast(targetStatus)) {
				ChunkRegion chunkRegion = new ChunkRegion(world, chunks, targetStatus, limChunkGen.getChunkDistance());
				ci.setReturnValue(limChunkGen.populateNoise(chunkRegion, targetStatus, executor, world, generator, structureTemplateManager, lightingProvider, fullChunkConverter, chunks, chunk2, regenerate).thenApply(chunk -> {
					if (chunk instanceof ProtoChunk protoChunk) {
						BelowZeroRetrogen belowZeroRetrogen = protoChunk.getBelowZeroRetrogen();
						if (belowZeroRetrogen != null) {
							BelowZeroRetrogen.replaceOldBedrock(protoChunk);
							if (belowZeroRetrogen.hasBedrockHoles()) {
								belowZeroRetrogen.applyBedrockMask(protoChunk);
							}
						}
						protoChunk.setStatus(targetStatus);
					}
					return Either.left(chunk);
				}));
			}
		}
	}

}
