package net.ludocrypt.limlib.impl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.ludocrypt.limlib.impl.shader.PostProcesserManager;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public class LimlibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(PostProcesserManager.INSTANCE);
	}

}
