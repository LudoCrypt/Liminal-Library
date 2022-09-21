package net.ludocrypt.limlib.render.mixin.chunk;

import java.util.Queue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Queues;

import net.ludocrypt.limlib.render.access.ChunkBuilderAccess;
import net.minecraft.client.render.chunk.ChunkBuilder;

@Mixin(ChunkBuilder.class)
public class ChunkBuilderMixin implements ChunkBuilderAccess {

	@Unique
	private final Queue<Runnable> renderUploadQueue = Queues.newConcurrentLinkedQueue();

	@Override
	public void uploadRenderMap() {
		Runnable runnable;
		while ((runnable = (Runnable) this.getRenderUploadQueue().poll()) != null) {
			runnable.run();
		}
	}

	@Override
	public Queue<Runnable> getRenderUploadQueue() {
		return renderUploadQueue;
	}

}
