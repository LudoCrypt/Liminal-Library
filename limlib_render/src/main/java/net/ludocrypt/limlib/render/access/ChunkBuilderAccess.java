package net.ludocrypt.limlib.render.access;

import java.util.Queue;

public interface ChunkBuilderAccess {

	public void uploadRenderMap();

	public Queue<Runnable> getRenderUploadQueue();

}
