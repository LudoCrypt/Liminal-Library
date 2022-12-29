package net.ludocrypt.limlib.render.special;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.render.mixin.registry.RegistryAccessor;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Holder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public abstract class SpecialModelRenderer {

	public static final RegistryKey<Registry<SpecialModelRenderer>> SPECIAL_MODEL_RENDERER_KEY = RegistryAccessor.callCreateRegistryKey("limlib/special_model_renderer");
	public static final Registry<SpecialModelRenderer> SPECIAL_MODEL_RENDERER = RegistryAccessor.callRegisterDefaulted(SPECIAL_MODEL_RENDERER_KEY, "limlib:special_model_renderer", SpecialModelRenderer::getRegistryHolderReference, registry -> TexturedSpecialModelRenderer.TEXTURED);

	@Environment(EnvType.CLIENT)
	public abstract void setup(MatrixStack matrices, ShaderProgram shader);

	private final Holder.Reference<SpecialModelRenderer> registryHolderReference = SPECIAL_MODEL_RENDERER.createIntrusiveHolder(this);

	public Holder.Reference<SpecialModelRenderer> getRegistryHolderReference() {
		return registryHolderReference;
	}

	public static void init() {
		Registry.register(SPECIAL_MODEL_RENDERER, new Identifier("limlib", "textured"), TexturedSpecialModelRenderer.TEXTURED);
	}
}
