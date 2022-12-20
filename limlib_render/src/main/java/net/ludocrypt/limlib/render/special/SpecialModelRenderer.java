package net.ludocrypt.limlib.render.special;

import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.render.mixin.registry.BuiltinRegistriesAccessor;
import net.ludocrypt.limlib.render.mixin.registry.RegistryAccessor;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public abstract class SpecialModelRenderer {

	public static final RegistryKey<Registry<SpecialModelRenderer>> SPECIAL_MODEL_RENDERER_KEY = RegistryAccessor.callCreateRegistryKey("limlib/special_model_renderer");
	public static final Registry<SpecialModelRenderer> SPECIAL_MODEL_RENDERER = BuiltinRegistriesAccessor.callAddRegistry(SPECIAL_MODEL_RENDERER_KEY, (registry) -> BuiltinRegistries.register(registry, RegistryKey.of(SPECIAL_MODEL_RENDERER_KEY, new Identifier("limlib", "default")), new TexturedSpecialModelRenderer(TextureManager.MISSING_IDENTIFIER)));

	@SuppressWarnings("unchecked")
	public static final SimpleRegistry<Codec<? extends SpecialModelRenderer>> SPECIAL_MODEL_RENDERER_CODEC = (SimpleRegistry<Codec<? extends SpecialModelRenderer>>) (Object) FabricRegistryBuilder.createSimple(Codec.class, new Identifier("limlib", "limlib_special_model_renderer")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Codec<SpecialModelRenderer> CODEC = SPECIAL_MODEL_RENDERER_CODEC.getCodec().dispatchStable(SpecialModelRenderer::getCodec, Function.identity());

	public abstract Codec<? extends SpecialModelRenderer> getCodec();

	@Environment(EnvType.CLIENT)
	public abstract void setup(MatrixStack matrices, ShaderProgram shader);

}
