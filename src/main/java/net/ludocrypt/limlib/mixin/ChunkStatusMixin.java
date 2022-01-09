package net.ludocrypt.limlib.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.datafixers.util.Either;

import net.ludocrypt.limlib.api.world.NbtChunkGenerator;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.BelowZeroRetrogen;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(ChunkStatus.class)
public class ChunkStatusMixin {

	@Inject(method = "method_38284(Lnet/minecraft/world/chunk/ChunkStatus;Ljava/util/concurrent/Executor;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/structure/StructureManager;Lnet/minecraft/server/world/ServerLightingProvider;Ljava/util/function/Function;Ljava/util/List;Lnet/minecraft/world/chunk/Chunk;Z)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
	private static void limlib$noise(ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureManager structureManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> function, List<Chunk> chunks, Chunk chunk2, boolean bl, CallbackInfoReturnable<CompletableFuture<?>> ci) {
		if (bl || !chunk2.getStatus().isAtLeast(targetStatus)) {
			if (generator instanceof NbtChunkGenerator nbtChunkGenerator) {
				ci.setReturnValue(((NbtChunkGenerator) generator).populateNoise(executor, chunk2, targetStatus, world, new ChunkRegion(world, chunks, targetStatus, nbtChunkGenerator.getChunkRadius()), structureManager, lightingProvider).thenApply(chunk -> {
					if (chunk instanceof ProtoChunk protoChunk) {
						BelowZeroRetrogen belowZeroRetrogen = protoChunk.getBelowZeroRetrogen();
						if (belowZeroRetrogen != null) {
							BelowZeroRetrogen.replaceOldBedrock(protoChunk);
							if (belowZeroRetrogen.hasMissingBedrock()) {
								belowZeroRetrogen.fillColumnsWithAirIfMissingBedrock(protoChunk);
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
