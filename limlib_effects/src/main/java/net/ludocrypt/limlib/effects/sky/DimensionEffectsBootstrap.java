package net.ludocrypt.limlib.effects.sky;

import net.minecraft.world.gen.BootstrapContext;

/**
 * Bootstrap Registration for Dimension Effects'
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with
 * {@value #ENTRYPOINT_KEY} key.
 * <p>
 */
public interface DimensionEffectsBootstrap {

	/**
	 * Represents the key which this entrypoint is defined with, whose value is
	 * {@value}.
	 */
	public static String ENTRYPOINT_KEY = "limlib:dimension_effects_bootstrap";

	/**
	 * Registers everything.
	 */
	void registerDimensionEffects(BootstrapContext<DimensionEffects> context);

}
