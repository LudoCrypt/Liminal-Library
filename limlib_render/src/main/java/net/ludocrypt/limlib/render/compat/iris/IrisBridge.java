package net.ludocrypt.limlib.render.compat.iris;

import org.quiltmc.loader.api.QuiltLoader;

public class IrisBridge {

	public static final boolean IRIS_LOADED = QuiltLoader.isModLoaded("iris");

	public static boolean areShadersInUse() {
//		if (IRIS_LOADED) {
//			IrisContained.areShadersInUse();
//		}
		return false;
	}

	public static boolean isHandRenderingActive() {
//		if (IRIS_LOADED) {
//			IrisContained.isHandRenderingActive();
//		}
		return false;
	}

}
