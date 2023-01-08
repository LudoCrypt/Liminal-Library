package net.ludocrypt.limlib.render.special;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.mojang.serialization.Lifecycle;

import net.ludocrypt.limlib.render.mixin.registry.RegistriesAccessor;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public abstract class SpecialModelRenderer {

	public static final RegistryKey<Registry<SpecialModelRenderer>> SPECIAL_MODEL_RENDERER_KEY = RegistryKey.ofRegistry(new Identifier("limlib/special_model_renderer"));
	public static final Registry<SpecialModelRenderer> SPECIAL_MODEL_RENDERER = RegistriesAccessor.callRegisterSimple(SPECIAL_MODEL_RENDERER_KEY, Lifecycle.stable(), registry -> TexturedSpecialModelRenderer.TEXTURED);

	@ClientOnly
	public abstract void setup(MatrixStack matrices, ShaderProgram shader);

}
