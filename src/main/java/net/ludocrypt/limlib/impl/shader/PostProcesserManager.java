package net.ludocrypt.limlib.impl.shader;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.profiler.Profiler;

public final class PostProcesserManager implements ResourceReloader {

	public static final PostProcesserManager INSTANCE = new PostProcesserManager();
	public static final Identifier RESOURCE_KEY = new Identifier("limlib:shaders");

	private final Set<PostProcesser> shaders = new ReferenceOpenHashSet<>();

	public PostProcesser find(Identifier location) {
		PostProcesser ret = new PostProcesser(location);
		shaders.add(ret);
		return ret;
	}

	public void onResolutionChanged(int newWidth, int newHeight) {

		if (!shaders.isEmpty()) {

			for (PostProcesser shader : shaders) {

				if (shader.isInitialized()) {
					MinecraftClient client = MinecraftClient.getInstance();
					shader.shader
						.setupDimensions(client.getWindow().getFramebufferWidth(),
							client.getWindow().getFramebufferHeight());
				}

			}

		}

	}

	public void dispose(PostProcesser shader) {
		shader.release();
		shaders.remove(shader);
	}

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler,
			Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
			applyProfiler.startTick();
			applyProfiler.push("listener");

			for (PostProcesser shader : shaders) {
				shader.init(manager);
			}

			applyProfiler.pop();
			applyProfiler.endTick();
		}, applyExecutor);
	}

}
