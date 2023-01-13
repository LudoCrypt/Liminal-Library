package net.ludocrypt.limlib.registry.registration;

/**
 * Bootstrap Registration for World's
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with
 * {@value #ENTRYPOINT_KEY} key.
 * <p>
 */
public interface DimensionBootstrap {

	/**
	 * Represents the key which this entrypoint is defined with, whose value is
	 * {@value}.
	 */
	public static String ENTRYPOINT_KEY = "limlib:dimension_bootstrap";

	/**
	 * Registers everything.
	 */
	void register();

}
