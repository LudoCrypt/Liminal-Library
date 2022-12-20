package net.ludocrypt.limlib.render.compat;

import org.quiltmc.loader.api.QuiltLoader;

public class SodiumBridge {

	public static final boolean SODIUM_LOADED = QuiltLoader.isModLoaded("sodium");

}
