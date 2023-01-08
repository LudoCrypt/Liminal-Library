package net.ludocrypt.limlib.effects.sound;

import net.minecraft.world.gen.BootstrapContext;

/**
 * Bootstrap Registration for Sound Effects'
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with
 * {@value #ENTRYPOINT_KEY} key.
 * <p>
 */
public interface SoundEffectsBootstrap {

	/**
	 * Represents the key which this entrypoint is defined with, whose value is
	 * {@value}.
	 */
	public static String ENTRYPOINT_KEY = "limlib:sound_effects_bootstrap";

	/**
	 * Registers everything.
	 */
	void register(BootstrapContext<SoundEffects> context);

}
