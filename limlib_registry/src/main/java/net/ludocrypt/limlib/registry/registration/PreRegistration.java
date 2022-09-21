package net.ludocrypt.limlib.registry.registration;

/**
 * A built-in registry helper.
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with
 * {@value #ENTRYPOINT_KEY} key.
 * <p>
 * Currently, it is executed in {@link BuiltinRegistries}, before the static
 * block, ie before all default registry values are set.
 *
 */
public interface PreRegistration {

	/**
	 * Represents the key which this entrypoint is defined with, whose value is
	 * {@value}.
	 */
	public static String ENTRYPOINT_KEY = "limlib:pre_registration";

	/**
	 * Registers everything.
	 */
	void register();

}
