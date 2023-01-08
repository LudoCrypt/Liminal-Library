package net.ludocrypt.limlib.render.skybox;

import net.minecraft.world.gen.BootstrapContext;

/**
 * Bootstrap Registration for Skybox's
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with
 * {@value #ENTRYPOINT_KEY} key.
 * <p>
 */
public interface SkyboxBootstrap {

	/**
	 * Represents the key which this entrypoint is defined with, whose value is
	 * {@value}.
	 */
	public static String ENTRYPOINT_KEY = "limlib:skybox_bootstrap";

	/**
	 * Registers everything.
	 */
	void register(BootstrapContext<Skybox> context);

}
