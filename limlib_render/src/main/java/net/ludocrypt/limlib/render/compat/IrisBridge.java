package net.ludocrypt.limlib.render.compat;

import org.quiltmc.loader.api.QuiltLoader;

public class IrisBridge {

	public static final boolean IRIS_LOADED = QuiltLoader.isModLoaded("iris");

	public static boolean areShadersInUse() {
		return false;
	}

	public static boolean isHandRenderingActive() {
		return false;
	}

}
