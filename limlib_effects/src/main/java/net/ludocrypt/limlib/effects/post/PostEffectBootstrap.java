package net.ludocrypt.limlib.effects.post;

import net.minecraft.world.gen.BootstrapContext;

/**
 * Bootstrap Registration for Post Effect's
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with
 * {@value #ENTRYPOINT_KEY} key.
 * <p>
 */
public interface PostEffectBootstrap {

	/**
	 * Represents the key which this entrypoint is defined with, whose value is
	 * {@value}.
	 */
	public static String ENTRYPOINT_KEY = "limlib:post_effect_bootstrap";

	/**
	 * Registers everything.
	 */
	void registerPostEffects(BootstrapContext<PostEffect> context);

}
